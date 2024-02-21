package co.yedam.perPro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//전적객체 생성
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Records {
	private String id;
	private int bot1_win;
	private int bot1_lose;
	private int bot2_win;
	private int bot2_lose;
	private int bot3_win;
	private int bot3_lose;
	private int total_win;
	private int total_lose;
}
