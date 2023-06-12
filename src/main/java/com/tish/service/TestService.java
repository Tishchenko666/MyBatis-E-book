package com.tish.service;

//import com.google.api.services.forms.v1.model.Form;

import com.google.api.services.drive.model.Permission;
import com.google.api.services.drive.model.PermissionList;
import com.google.api.services.forms.v1.model.*;
import com.tish.config.AccessToken;
import com.tish.config.Boilerplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class TestService {

	private final AccessToken accessToken;

	public TestService(@Autowired AccessToken accessToken) {
		this.accessToken = accessToken;
	}


	public Map<String, String> openTest() {
		Map<String, String> map = new HashMap<>();
		try {
			//0- access token, from the previous chapter
			String token = accessToken.getAccessToken();

			//1- create a new form
			String formId = createNewForm(token);

			//2- transform it into a quiz
			transformInQuiz(formId, token);

			//3- add questions
			addItemToQuiz(
					"Which of these singers was not a member of Destiny's Child?",
					Arrays.asList("Kelly Rowland", "Beyoncè", "Rihanna", "Michelle Williams"),
					"Rihanna",
					formId,
					token
			);
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

	private static void readResponses(String formId, String token) {
		try {
			ListFormResponsesResponse response = Boilerplate.getFormsService().forms().responses().list(formId).setOauthToken(token).execute();
			System.out.println(response.toPrettyString());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String createNewForm(String token) throws IOException {
		Form form = new Form();
		form.setInfo(new Info());
		form.getInfo().setTitle("New Form Quiz Created from Java");

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
		grading.setPointValue(2);
		grading.setCorrectAnswers(new CorrectAnswers());
		grading.getCorrectAnswers().setAnswers(new ArrayList<>());
		grading.getCorrectAnswers().getAnswers().add(new CorrectAnswer());
		grading.getCorrectAnswers().getAnswers().get(0).setValue(correctAnswer);
		Feedback whenRight = new Feedback();
		whenRight.setText("Yeah!");
		Feedback whenWrong = new Feedback();
		whenWrong.setText("Wrong Answer");
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
