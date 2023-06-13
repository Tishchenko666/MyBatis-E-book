package com.tish.constant;

public class TestConstant {

	public static String currentFormId = "";
	public static String currentToken = "";

	public static final String READ_TESTS_BY_ID = "select t.id, m.title, material_id, type, question, variants, answer from tests t inner join materials m on m.id = t.material_id where material_id=?";

	public static final String SAVE_TEST_RESULT = "insert into users_results (user_id, question_id, answer, result) values (?, ?, ?, ?)";
}
