package co.yedam.perPro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Boards {
	private String rownum;
	private String code;
	private String id;
	private String head;
	private String title;
	private String main_text;
	private String answer_check;
	private String answer;
	private String write_date;
}
