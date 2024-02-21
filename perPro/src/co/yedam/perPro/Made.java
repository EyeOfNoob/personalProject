package co.yedam.perPro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//족보 객체
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Made {
	private int made_no;
	private String name;
	private int score;
	private String guide; 
}
