package co.yedam.perPro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 회원 객체 생성
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Members {
	private String type;
	private String name;
	private String id;
	private String pw;
	private String mail;
	private String phone;
	private String joinDate;
	private String status;
	private int coin;
	private int loginFail;
}
