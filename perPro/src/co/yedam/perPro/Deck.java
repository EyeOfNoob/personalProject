package co.yedam.perPro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//덱 객체 생성
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Deck {
	private int card_no;
	private String card;
}
