package co.yedam.perPro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//	봇객체 생성
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bots {
	private int bot_no;
	private String bot_name;
	private int win;
	private int lose;
	private int tac_no;
}
