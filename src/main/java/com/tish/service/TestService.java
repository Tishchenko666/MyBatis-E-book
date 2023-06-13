package com.tish.service;

//import com.google.api.services.forms.v1.model.Form;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.drive.model.PermissionList;
import com.google.api.services.forms.v1.model.*;
import com.tish.config.AccessToken;
import com.tish.config.Boilerplate;
import com.tish.dao.TestDao;
import com.tish.dao.UserDao;
import com.tish.entity.Test;
import com.tish.entity.UserResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TestService {

	private final AccessToken accessToken;
	private final TestDao testDao;
	private final UserDao userDao;
	private final ObjectMapper mapper;

	public TestService(@Autowired AccessToken accessToken,
					   @Autowired TestDao testDao,
					   @Autowired UserDao userDao,
					   @Autowired ObjectMapper mapper) {
		this.accessToken = accessToken;
		this.testDao = testDao;
		this.userDao = userDao;
		this.mapper = mapper;
	}


	public Map<String, String> openTest(Integer materialId) {
		Map<String, String> map = new HashMap<>();
		try {
			//0- access token, from the previous chapter
			String token = accessToken.getAccessToken();

			List<Test> tests = testDao.readTestsByMaterialId(materialId);

			if (tests.isEmpty()) {
				return null;
			}
			//1- create a new form
			String formId = createNewForm(token, tests.get(0).getTitle());

			//2- transform it into a quiz
			transformInQuiz(formId, token);

			//3- add questions
			Collections.reverse(tests);
			for (Test test : tests) {
				LinkedHashMap<String, String> testMap = mapper.readValue(test.getVariants(), LinkedHashMap.class);
				addItemToQuiz(
						test.getQuestion(),
						new ArrayList<>(testMap.values()),
						testMap.get(test.getAnswer()),
						formId,
						token
				);
			}


			map.put("formId", formId);
			map.put("token", token);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return map;

	}

	public boolean publishForm(String formId, String token) {

		try {

			PermissionList list = Boilerplate.getDriveService().permissions().list(formId).setOauthToken(token).execute();

			if (list.getPermissions().stream().noneMatch((it) -> it.getRole().equals("reader"))) {
				Permission body = new Permission();
				body.setRole("reader");
				body.setType("anyone");
				Boilerplate.getDriveService().permissions().create(formId, body).setOauthToken(token).execute();
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void readResponses(String formId, String token, String login, Integer materialId) {
		try {
			if (login.isEmpty()) {
				return;
			}

			Integer userId = userDao.checkIfUserExists(login).getId();
			ListFormResponsesResponse response = Boilerplate.getFormsService().forms().responses().list(formId).setOauthToken(token).execute();
			List<Test> tests = testDao.readTestsByMaterialId(materialId);
			List<Answer> answers = response.getResponses().get(0).getAnswers().values().stream().collect(Collectors.toList());
			for (int i = 0; i < tests.size(); i++) {
				UserResult userResult = new UserResult();
				userResult.setUserId(userId);
				userResult.setQuestionId(tests.get(i).getId());
				userResult.setAnswer(answers.get(i).getTextAnswers().getAnswers().get(0).getValue());
				userResult.setResult(answers.get(i).getGrade().getScore() == null ? 0 : 1);
				testDao.saveTestResult(userResult);
			}

			response.getResponses().get(0).getAnswers().values().forEach(answer -> System.out.println(answer.getTextAnswers().getAnswers().get(0).getValue()));
			response.getResponses().get(0).getAnswers().values().forEach(answer -> System.out.println(answer.getGrade().getScore()));
			System.out.println(response.toPrettyString());

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String createNewForm(String token, String title) throws IOException {
		Form form = new Form();
		form.setInfo(new Info());
		form.getInfo().setTitle(title);

		form = Boilerplate.getFormsService().forms().create(form)
				.setAccessToken(token)
				.execute();
		return form.getFormId();
	}


	private void transformInQuiz(String formId, String token) throws IOException {
		BatchUpdateFormRequest batchRequest = new BatchUpdateFormRequest();
		Request request = new Request();
		request.setUpdateSettings(new UpdateSettingsRequest());
		request.getUpdateSettings().setSettings(new FormSettings());
		request.getUpdateSettings().getSettings().setQuizSettings(new QuizSettings());
		request.getUpdateSettings().getSettings().getQuizSettings().setIsQuiz(true);
		request.getUpdateSettings().setUpdateMask("quizSettings.isQuiz");
		batchRequest.setRequests(Collections.singletonList(request));
		Boilerplate.getFormsService().forms().batchUpdate(formId, batchRequest)
				.setAccessToken(token).execute();
	}

	private void addItemToQuiz(
			String questionText,
			List<String> answers,
			String correctAnswer,
			String formId, String token) throws IOException {

		BatchUpdateFormRequest batchRequest = new BatchUpdateFormRequest();
		Request request = new Request();

		Item item = new Item();
		item.setTitle(questionText);

		item.setQuestionItem(new QuestionItem());
		Question question = new Question();
		question.setRequired(true);
		question.setChoiceQuestion(new ChoiceQuestion());
		question.getChoiceQuestion().setType("RADIO");

		List<Option> options = new ArrayList<>();
		for (String answer : answers) {
			Option option = new Option();
			option.setValue(answer);
			options.add(option);
		}
		question.getChoiceQuestion().setOptions(options);

		Grading grading = new Grading();
		grading.setPointValue(1);
		grading.setCorrectAnswers(new CorrectAnswers());
		grading.getCorrectAnswers().setAnswers(new ArrayList<>());
		grading.getCorrectAnswers().getAnswers().add(new CorrectAnswer());
		grading.getCorrectAnswers().getAnswers().get(0).setValue(correctAnswer);
		Feedback whenRight = new Feedback();
		whenRight.setText("Well Done!");
		Feedback whenWrong = new Feedback();
		whenWrong.setText("Sorry!");
		grading.setWhenRight(whenRight);
		grading.setWhenWrong(whenWrong);
		question.setGrading(grading);

		item.getQuestionItem().setQuestion(question);
		request.setCreateItem(new CreateItemRequest());
		request.getCreateItem().setItem(item);
		request.getCreateItem().setLocation(new Location());
		request.getCreateItem().getLocation().setIndex(0);

		batchRequest.setRequests(Collections.singletonList(request));

		Boilerplate.getFormsService().forms().batchUpdate(formId, batchRequest)
				.setAccessToken(token).execute();
	}

}
