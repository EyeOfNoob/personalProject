package co.yedam.perPro;

import java.util.List;
import java.util.Scanner;

public class InPokerApp {
	public static void main(String[] args) {
		InPokerDAO pdao = new InPokerDAO();
		Scanner scn = new Scanner(System.in);
		boolean run = true;
		boolean userC = false;
		boolean manaC = false;
		boolean sysC = false;
//		String mode = "";
		int menu = 0;
		Members whoAmI = new Members();

//		로그인
		while (run) {
			System.out.println("1.로그인 2.회원가입 0.종료");
			System.out.print("입력>> ");
			try {
				menu = Integer.parseInt(scn.nextLine());
				if (menu > 2) {
					System.out.println("메뉴에 맞는 번호를 입력해 주세요.");
					continue;
				}
			} catch (Exception e) {
				System.out.println("메뉴에 맞는 번호를 입력해 주세요.");
				continue;
			}
//			scn.nextLine();
			String id = "";
			String pw = "";
//			로그인
			if (menu == 1) {
				System.out.print("아이디>> ");
				id = scn.nextLine();
				System.out.print("비밀번호>> ");
				pw = scn.nextLine();
				int loginC = pdao.loginMem(id, pw);
				if (loginC == -3) {
					System.out.println("보호처리된 계정입니다. 운영진에게 문의해주세요.");
//					보호처리 해제요청 작성필요
					System.out.println("보호처리 해제요청을 하시겠습니까?(y/n)");
					System.out.print("입력>> ");
					String lockCk = scn.nextLine();
					if (lockCk.equals("y")) {
						String head = "보호처리";
						String title = "";
						String body = "";
						System.out.println("제목을 입력해주세요.");
						System.out.print("입력>> ");
						title = scn.nextLine();
						System.out.println("내용을 입력해주세요.");
						System.out.print("입력>> ");
						body = scn.nextLine();
						String code = id + pdao.BoardNo("0", id);
						Boards bod = new Boards(null, code, id, head, title, body, null, null, null);
						if (pdao.insertBoard(bod)) {
							System.out.println("등록 성공");
							pdao.BoardNo("+", id);
						} else {
							System.out.println("오류 발생");
						}
						continue;
					}
					if (lockCk.equals("n")) {
						continue;
					} else {
						System.out.println("'y' 또는 'n'을 입력해주세요");
					}
				} else if (loginC == -4) {
					System.out.println("접속실패 5회로 계정이 보호처리되었습니다.");
//					보호처리 해제요청 작성필요
					System.out.println("보호처리 해제요청을 하시겠습니까?(y/n)");
					System.out.print("입력>> ");
					String lockCk = scn.nextLine();
					if (lockCk.equals("y")) {
						String head = "보호처리";
						String title = "";
						String body = "";
						System.out.println("제목을 입력해주세요.");
						System.out.print("입력>> ");
						title = scn.nextLine();
						System.out.println("내용을 입력해주세요.");
						System.out.print("입력>> ");
						body = scn.nextLine();
						String code = id + pdao.BoardNo("0", id);
						Boards bod = new Boards(null, code, id, head, title, body, null, null, null);
						if (pdao.insertBoard(bod)) {
							System.out.println("등록 성공");
							pdao.BoardNo("+", id);
						} else {
							System.out.println("오류 발생");
						}
						continue;
					}
					if (lockCk.equals("n")) {
						continue;
					} else {
						System.out.println("'y' 또는 'n'을 입력해주세요");
					}
				} else if (loginC == 1) {
					System.out.println("로그인 성공");
					whoAmI = pdao.whoMem(id, pw);
					if (whoAmI.getType().equals("user")) {
						userC = true;
					} else if (whoAmI.getType().equals("manager")) {
						manaC = true;
					} else if (whoAmI.getType().equals("sys")) {
						sysC = true;
					}
					run = false;
				} else if (loginC == 0) {
					System.out.println("비밀번호가 틀렸습니다.");
				} else if (loginC == -1) {
					System.out.println("존재하지 않는 아이디입니다.");
				} else if (loginC == -2) {
					System.out.println("오류 발생");
				}
//			회원가입
			} else if (menu == 2) {
				boolean nameCk = true;
				boolean idCk = true;
				boolean pwCk = true;
				String name = "";
				String user_id = "";
				String user_pw = "";
				String user_mail = "";
				String user_phone = "";
				while (nameCk) {
					System.out.print("이름>> ");
					name = scn.nextLine();
					if (name.equals("")) {
						System.out.println("이름에 공백을 사용할 수 없습니다.");
						continue;
					}
					nameCk = false;
				}
				while (idCk) {
					System.out.print("아이디>> ");
					user_id = scn.nextLine();
					if (user_id.equals("")) {
						System.out.println("아이디에 공백을 사용할 수 없습니다.");
						continue;
					} else if (pdao.idCheck(user_id)) {
						System.out.println("이미 등록된 아이디입니다.");
						continue;
					}
					idCk = false;
				}
				while (pwCk) {
					System.out.print("비밀번호>> ");
					user_pw = scn.nextLine();
					if (user_pw.equals("")) {
						System.out.println("비밀번호에 공백을 사용할 수 없습니다.");
						continue;
					}
					pwCk = false;
				}
				System.out.print("메일주소>> ");
				user_mail = scn.nextLine();
				System.out.print("전화번호>> ");
				user_phone = scn.nextLine();

				Members uMem = new Members(null, name, user_id, user_pw, user_mail, user_phone, null, null, 0, 0);

				if (pdao.insertMem(uMem, "user")) {
					System.out.println("정상적으로 등록.");
					pdao.BoardNo("f", user_id);
					pdao.insertRecord(user_id);
				} else {
					System.out.println("등록 에러.");
				}
//			종료
			} else if (menu == 0) {
				System.out.println("종료합니다.");
				run = false;
			}
		} // end of login

//		유저화면
		while (userC) {
			int menuC = 0;
			String urName = whoAmI.getName();
			String urId = whoAmI.getId();
//			System.out.println(urName + "님 반갑습니다.");
			System.out.println("==============유저 메뉴==============");
			System.out.println("1.게임시작 2.게임설명 3.전적조회 4.개인정보수정");
			System.out.println("5.고객센터 6.회원탈퇴 7.보유코인조회   0.종료");
			System.out.println("==================================");
			System.out.print("입력>> ");
			try {
				menuC = Integer.parseInt(scn.nextLine());
				if (menuC > 7) {
					System.out.println("메뉴에 맞는 번호를 입력해 주세요.");
					continue;
				}
			} catch (Exception e) {
				System.out.println("메뉴에 맞는 번호를 입력해 주세요.");
				continue;
			}
//			게임시작
			if (menuC == 1) {
				int betCoin = 0;
				int betCoin2 = 0;
				int totalCoin = 0;
				List<Members> list = pdao.getList("user", urId);
				for (Members mem : list) {
					totalCoin = mem.getCoin();
				}
				if (totalCoin < 10) {//보유 코인 체크
					System.out.println("보유 코인량이 기본 베팅금미만입니다. 게임에 참여하실수 없습니다.");
					System.out.println("추가 코인충전은 고객센터 일반문의에 남겨주세요.");
					continue;
				}
				List<Bots> bot = pdao.getBot("game");
				String bot_name = bot.get(0).getBot_name();
				int bot_no = bot.get(0).getBot_no();
				int bot_tactic = bot.get(0).getTac_no();
				int random_tactic = (int) (Math.random() * 10 + 1);
				if (random_tactic < 6) {
					bot_tactic = 4;
				}
				System.out.println("당신의 상대는 " + bot_name + "BOT 입니다.");
				System.out.println("기본 베팅금 10코인이 지불되었습니다.");

				Members mem = new Members();
				mem.setId(urId);
				mem.setCoin(totalCoin - 10);
				pdao.updateMem(mem, "sys");
				List<Deck> deck = pdao.shuffleDeck();// 셔플된 카드덱 생성
				System.out.print("딜러가 카드를 섞는 중입니다");
				String[] str = "...".split("");
				for (int i = 0; i < str.length; i++) {
					System.out.print(str[i]);
					try {
						Thread.sleep(400);
					} catch (InterruptedException ie) {
						Thread.currentThread().interrupt();
					}
				}
				System.out.println();

//				첫번째 턴
				list = pdao.getList("user", urId);
				for (Members mem1 : list) {
					totalCoin = mem1.getCoin();
				}
				System.out.println("┌─────────┐");
				System.out.println("  당신의 턴!  ");
				System.out.println("└─────────┘");
				System.out.println("당신의 첫번째 카드 : " + deck.get(0).getCard());
				int fTurn = 0;
				while (true) {
					System.out.println("행동을 선택해 주세요.");
					System.out.println("1.베팅 2.폴드 [" + urId + "님의 현재 손패 : " + deck.get(0).getCard() + " ]");
					System.out.print("입력>> ");
					try {
						fTurn = Integer.parseInt(scn.nextLine());
						if (fTurn > 2 || fTurn < 1) {
							System.out.println("메뉴에 맞는 번호를 입력해 주세요.");
							continue;
						} else {
							break;
						}
					} catch (Exception e) {
						System.out.println("메뉴에 맞는 번호를 입력해 주세요.");
						continue;
					}
				}
				if (fTurn == 1) { // 베팅 선택
					System.out.printf("%s 회원님의 현재 보유코인량은 %s입니다.", urId, totalCoin);
					System.out.println();

					while (true) {
						System.out.println("베팅할 코인을 입력해 주세요.");
						System.out.print("입력>> ");
						try {
							betCoin = Integer.parseInt(scn.nextLine());
							if (betCoin < 0 || betCoin == 0) {
								System.out.println("0이하의 코인은 베팅할 수 없습니다.");
								continue;
							} else if (betCoin > totalCoin) {
								System.out.println("보유량보다 많은 코인을 베팅할수 없습니다.");
								continue;
							} else {
								break;
							}
						} catch (Exception e) {
							System.out.println("숫자를 입력해 주세요.");
							continue;
						}
					}
					// 베팅한 코인 보유량에서 제외
					Members mem2 = new Members();
					mem2.setId(urId);
					mem2.setCoin(totalCoin - betCoin);
					pdao.updateMem(mem2, "sys");
				} else if (fTurn == 2) { // 폴드 선택
					System.out.println("폴드를 선언하셨습니다. 당신의 패배.");
					Records rcd = new Records();
					rcd.setId(urId);
					rcd.setTotal_lose(1);
					pdao.updateRcd(urId, bot_no, "lose");
					continue;
				}
//				출력 딜레이 생성
				try {
					Thread.sleep(500);
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
				}
//				봇의 베팅판단
				System.out.println("┌───────────────┐");
				System.out.println("  " + bot_name + " BOT의 턴!");
				System.out.println("└───────────────┘");
//				출력 딜레이 생성
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
				}
				String botBet = pdao.botTactic1(bot_tactic, deck.get(2).getCard());
				if (botBet.equals("베팅")) {
					System.out.println(bot_name + "BOT도 베팅을 하였습니다.");
				} else if (botBet.equals("폴드")) {
					System.out.println(bot_name + "BOT이 폴드를 선언했습니다. " + urId + "님의 승리!");
					list = pdao.getList("user", urId);
					for (Members mem1 : list) {
						totalCoin = mem1.getCoin();
					}

					Members mem2 = new Members();
					mem2.setId(urId);
					mem2.setCoin(totalCoin + betCoin + 20);
					pdao.updateMem(mem2, "sys");
					pdao.updateRcd(urId, bot_no, "win");
					continue;
				}
//				출력 딜레이 생성
				try {
					Thread.sleep(500);
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
				}
//				2번째 턴
				System.out.println("┌─────────┐");
				System.out.println("  당신의 턴! ");
				System.out.println("└─────────┘");
				System.out.println("당신의 두번째 카드 : " + deck.get(1).getCard());
				int sTurn = 0;
				while (true) {
					System.out.println("행동을 선택해 주세요.");
					System.out.println("1.베팅 2.폴드 [" + urId + "님의 현재 손패 : " + deck.get(0).getCard() + " , "
							+ deck.get(1).getCard() + " ]");
					System.out.print("입력>> ");
					try {
						sTurn = Integer.parseInt(scn.nextLine());
						if (sTurn > 2 || sTurn < 1) {
							System.out.println("메뉴에 맞는 번호를 입력해 주세요.");
							continue;
						} else {
							break;
						}
					} catch (Exception e) {
						System.out.println("메뉴에 맞는 번호를 입력해 주세요.");
						continue;
					}
				}
				if (sTurn == 1) { // 베팅 선택
					list = pdao.getList("user", urId);
					for (Members mem3 : list) {
						totalCoin = mem3.getCoin();
					}
					System.out.printf("%s 회원님의 현재 보유코인량은 %s입니다.", urId, totalCoin);
					System.out.println();
					while (true) {
						System.out.println("베팅할 코인을 입력해 주세요.");
						System.out.print("입력>> ");
						betCoin2 = Integer.parseInt(scn.nextLine());
						if (betCoin2 < 0 || betCoin2 == 0) {
							System.out.println("0이하의 코인은 베팅할 수 없습니다.");
							continue;
						} else if (betCoin2 > totalCoin) {
							System.out.println("보유량보다 많은 코인을 베팅할수 없습니다.");
							continue;
						} else {
							break;
						}
					}
					// 베팅한 코인 보유량에서 제외
					Members mem1 = new Members();
					mem1.setId(urId);
					mem1.setCoin(totalCoin - betCoin);
					pdao.updateMem(mem1, "sys");
				} else if (sTurn == 2) { // 폴드 선택
					System.out.println("폴드 선언으로 패배했습니다.");
					pdao.updateRcd(urId, bot_no, "lose");
					continue;
				}
//				출력 딜레이 생성
				try {
					Thread.sleep(500);
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
				}
//				봇 베팅판단2
				System.out.println("┌───────────────┐");
				System.out.println("  " + bot_name + " BOT의 턴!");
				System.out.println("└───────────────┘");
//				출력 딜레이 생성
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
				}
				String botBet2 = pdao.botTactic2(bot_tactic, deck.get(2).getCard(), deck.get(3).getCard());
				if (botBet2.equals("베팅")) {
					System.out.println(bot_name + "BOT도 베팅을 하였습니다.");
				} else if (botBet2.equals("폴드")) {
					System.out.println(bot_name + "BOT이 폴드를 선언했습니다. " + urId + "님의 승리!");
					list = pdao.getList("user", urId);
					for (Members mem1 : list) {
						totalCoin = mem1.getCoin();
					}
					Members mem2 = new Members();
					mem2.setId(urId);
					mem2.setCoin(totalCoin + betCoin2 + (betCoin + 10) * 2);
					pdao.updateMem(mem2, "sys");
					pdao.updateRcd(urId, bot_no, "win");
					continue;
				}
				int botScore = pdao.ckScore(deck.get(2).getCard(), deck.get(3).getCard());
				int urScore = pdao.ckScore(deck.get(0).getCard(), deck.get(1).getCard());
				String botMade = pdao.ckMade(deck.get(2).getCard(), deck.get(3).getCard());
				String urMade = pdao.ckMade(deck.get(0).getCard(), deck.get(1).getCard());
				boolean lose = botScore > urScore;
				boolean win = botScore < urScore;
//				출력 딜레이 생성
				try {
					Thread.sleep(300);
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
				}
				System.out.println(urId + "님의 메이드 : " + urMade + ", " + bot_name + "BOT의 메이드 : " + botMade);
				if (lose) {
					System.out.println(bot_name + "BOT이 더 높은 점수를 얻었습니다. 패배.");
					pdao.updateRcd(urId, bot_no, "lose");
				} else if (win) {
					list = pdao.getList("user", urId);
					for (Members mem1 : list) {
						totalCoin = mem1.getCoin();
					}
					Members mem2 = new Members();
					mem2.setId(urId);
					mem2.setCoin(totalCoin + (betCoin + betCoin2 + 10) * 2);
					pdao.updateMem(mem2, "sys");
					System.out.println(urId + "님이 더 높은 점수를 얻었습니다. 승리!");
					pdao.updateRcd(urId, bot_no, "win");
				}
//				결과집계
//				System.out.println(deck.get(2).getCard());
//				System.out.println(deck.get(3).getCard());

//			게임설명
			} else if (menuC == 2) {
				System.out.println("====================================[게 임 설 명]===================================");
				System.out.println(pdao.guide());
				System.out.println("=================================================================================");
				while (true) {
					System.out.println("족보를 보시겠습니까?(y/n)");
					System.out.print("입력>> ");
					String ckk = scn.nextLine();
					if (ckk.equals("y")) {
						List<Made> list = pdao.madeGuide();
						System.out.println("=====================================================");
						System.out.println("족보이름            점수     설명");
						System.out.println("-----------------------------------------------------");
						for (Made mad : list) {
							if (mad.getName().equals("플러시") || mad.getName().equals("페어")) {
								System.out.printf("%-16s", mad.getName());
							} else if (mad.getName().equals("스트레이트") || mad.getName().equals("백 스트레이트")) {
								System.out.printf("%-15s", mad.getName());
							} else {
								System.out.printf("%-14s", mad.getName());
							}
							System.out.printf("%-6d", mad.getScore());
							System.out.printf("%-25s", mad.getGuide());
							System.out.println();
						}
						System.out.println("=====================================================");
						break;
					} else if (ckk.equals("n")) {
						break;
					} else {
						System.out.println("y 또는 n을 입력해주세요.");
						continue;
					}
				}
//			전적조회
			} else if (menuC == 3) {
				List<Records> list = pdao.getRecord(urId);
//				System.out.println("BOT1승리 BOT1패배 BOT2승리 BOT2패배 BOT3승리 BOT3패배 종합승리 종합패배");
				System.out.println("승리 패배 승률");
				for (Records rcd : list) {
					int tW = rcd.getTotal_win();
					int tL = rcd.getTotal_lose();
					int totalG = tW + tL;
					float odds = ((float) (tW) / (float) (totalG)) * 100;
					System.out.printf("%-4s", tW);
					System.out.printf("%-4s", tL);
					System.out.print((int) odds + "%");
					System.out.println();
				}
//			개인정보수정
			} else if (menuC == 4) {
				System.out.println("공백 입력시 해당항목은 수정되지 않습니다.");
				System.out.println("새로 사용할 비밀번호를 입력해주세요.");
				System.out.print("입력>> ");
				String pw = scn.nextLine();
				System.out.println("수정할 전화번호를 입력해주세요.");
				System.out.print("입력>> ");
				String ph = scn.nextLine();
				System.out.println("수정할 메일주소를 입력해주세요.");
				System.out.print("입력>> ");
				String em = scn.nextLine();
				Members mem = new Members();
				mem.setId(urId);
				mem.setPw(pw);
				mem.setPhone(ph);
				mem.setMail(em);
				if (pdao.updateMem(mem, "user")) {
					System.out.println("수정 완료");
				} else {
					System.out.println("수정중 오류 발생");
				}

//			고객센터
			} else if (menuC == 5) {
				int subMenu = 0;
				System.out.println("1.문의사항 등록 2.내문의사항 조회");
				System.out.print("입력>> ");
				try {
					subMenu = Integer.parseInt(scn.nextLine());
					if (subMenu > 2 || subMenu < 1) {
						System.out.println("메뉴에 맞는 번호를 입력해 주세요.");
						continue;
					}
				} catch (Exception e) {
					System.out.println("메뉴에 맞는 번호를 입력해 주세요.");
					continue;
				}
//				문의사항 등록
				if (subMenu == 1) {
					int qnaNo = 0;
					String head = "";
					String title = "";
					String body = "";
					System.out.println("1.건의사항 2.버그제보 3.일반문의");
					System.out.print("입력>>");
					try {
						qnaNo = Integer.parseInt(scn.nextLine());
						if (qnaNo > 3 && subMenu < 1) {
							System.out.println("메뉴에 맞는 번호를 입력해 주세요.");
							continue;
						}
					} catch (Exception e) {
						System.out.println("메뉴에 맞는 번호를 입력해 주세요.");
						continue;
					}
					if (qnaNo == 1) {
						head = "건의사항";
					} else if (qnaNo == 2) {
						head = "버그제보";
					} else if (qnaNo == 3) {
						head = "일반문의";
					}
					System.out.println("제목을 입력해주세요.");
					System.out.print("입력>> ");
					title = scn.nextLine();
					System.out.println("내용을 입력해주세요.");
					System.out.print("입력>> ");
					body = scn.nextLine();
					String code = urId + pdao.BoardNo("0", urId);
					Boards bod = new Boards(null, code, urId, head, title, body, null, null, null);
					if (pdao.insertBoard(bod)) {
						System.out.println("등록 성공");
						pdao.BoardNo("+", urId);
					} else {
						System.out.println("오류 발생");
					}

//				문의사항 관리
				} else if (subMenu == 2) {// 전체내용 조회
					int page = 1;
					String inNo = "";
					while (true) {
						System.out.println("=================================================");
						System.out.println("번호   분류   제목                    처리상황   작성일자");
						System.out.println("-------------------------------------------------");
						List<Boards> list = pdao.getBoard("userAll", urId, page);
						for (Boards bod : list) {
							String date = bod.getWrite_date();
							System.out.printf(" %-3s", bod.getRownum());
							System.out.printf("%-5s", bod.getHead());
							System.out.printf("%-22s", bod.getTitle());
							String ck = "";
							if (bod.getAnswer_check().equals("X")) {
								ck = "처리중";
							} else if (bod.getAnswer_check().equals("O")) {
								ck = "답변완료";
							}
							System.out.printf("%-5s", ck);
							System.out.printf("%-12s", date.substring(0, 11));
							System.out.println();
						}
						System.out.println("=================================================");
						int totalCnt = pdao.getTotalCnt(urId);
						int lastPage = (int) Math.ceil(totalCnt / 5.0);
						System.out.print(" <<");
						for (int i = 1; i <= lastPage; i++) {
							System.out.printf("%3d", i);
						}
						System.out.println(" >> ");
						int loMenu = 0;
						System.out.println("1.페이지이동 2.내용조회 3.수정 4.삭제 0.돌아가기");
						System.out.print("입력>> ");
						loMenu = Integer.parseInt(scn.nextLine());
						try {
							if (loMenu > 4 && loMenu < 0) {
								System.out.println("메뉴에 맞는 번호를 입력해 주세요.");
								continue;
							}
						} catch (Exception e) {
							System.out.println("메뉴에 맞는 번호를 입력해 주세요.");
							continue;
						}
						if (loMenu == 1) {// 페이지이동
							System.out.print("페이지>> ");
							page = Integer.parseInt(scn.nextLine());
						} else if (loMenu == 2) {// 내용조회
							System.out.println("조회할 글번호를 입력해주세요");
							System.out.print("입력>> ");
							page = Integer.parseInt(scn.nextLine());
							System.out.println("======================================================");
							System.out.println("번호   분류   제목                    작성일자       작성시간");
							System.out.println("------------------------------------------------------");
							List<Boards> getList = pdao.getBoard("user", urId, page);
							for (Boards bod : getList) {
								String date = bod.getWrite_date();
								System.out.printf(" %-3s", bod.getRownum());
								System.out.printf("%-5s", bod.getHead());
								System.out.printf("%-22s", bod.getTitle());
								System.out.printf("%-12s", date.substring(0, 11));
								System.out.printf("%-12s", date.substring(11));
								System.out.println();
								System.out.println("-본문---------------------------------------------------");
								System.out.printf(" %s", bod.getMain_text());
								System.out.println();
								System.out.println();
								System.out.println("-답변---------------------------------------------------");
								String ans = "";
//								ans = bod.getAnswer();
								if (bod.getAnswer_check().equals("X")) {
									ans = " 처리중입니다. ";
								} else if (bod.getAnswer_check().equals("O")) {
									ans = bod.getAnswer();
								}
								System.out.printf("%s", ans);
								System.out.println();
								System.out.println();
							}
							System.out.println("======================================================");
							break;
						} else if (loMenu == 3) {// 수정
							System.out.println("수정할 글번호를 입력해주세요");
							System.out.print("입력>> ");
							int no = Integer.parseInt(scn.nextLine()) - 1;
							String pick = list.get(no).getCode();
							System.out.println("수정할 제목을 입력해주세요");
							System.out.print("입력>> ");
							String tit = scn.nextLine();
							System.out.println("수정할 내용를 입력해주세요");
							System.out.print("입력>> ");
							String tex = scn.nextLine();

							Boards bod = new Boards();
							bod.setTitle(tit);
							bod.setMain_text(tex);

							if (pdao.updateBod("user", bod, pick)) {
								System.out.println("수정 성공");
							} else {
								System.out.println("오류 발생");
							}
						} else if (loMenu == 4) {// 삭제
							System.out.println("삭제할 글번호를 입력해주세요");
							System.out.print("입력>> ");
							int no = Integer.parseInt(scn.nextLine()) - 1;
							String pick = list.get(no).getCode();
							Boards bod = new Boards();
							bod.setCode(pick);
							if (pdao.deleteBod(bod)) {
								System.out.println("삭제 성공");
							} else {
								System.out.println("삭제중 오류발생");
							}
						} else if (loMenu == 0) {// 돌아가기
							break;
						}
					}
				}
//			회원탈퇴
			} else if (menuC == 6) {
				while (true) {
					System.out.println("-------------------------------");
					System.out.println("탈퇴시 전적,문의사항 등도 모두 삭제됩니다.");
					System.out.println("-------------------------------");
					System.out.println("정말로 탈퇴 하시겠습니까?(y/n)");
					System.out.print("입력>> ");
					String ckk = scn.nextLine();
					if (ckk.equals("y")) {
						Members mem = new Members();
						mem.setId(urId);
						if (pdao.deleteMem(mem)) {
							System.out.println("탈퇴 완료");
							System.out.println("이용해주셔서 감사합니다.");
						} else {
							System.out.println("탈퇴중 오류발생");
						}
						userC = false;
						break;
					} else if (ckk.equals("n")) {
						break;
					} else {
						System.out.println("'y' 또는 'n'을 입력해주세요");
					}
				}
//			보유코인 조회
			} else if (menuC == 7) {
				List<Members> list = pdao.getList("user", urId);
				for (Members mem : list) {
					System.out.printf("%s 회원님의 현재 보유코인량은 %s입니다.", urId, mem.getCoin());
					System.out.println();
				}
//			종료
			} else if (menuC == 0) {
				System.out.println("종료합니다.");
				userC = false;
			}
		} // end of userMenu

//		운영자화면
		while (manaC) {
			int menuM = 0;
			System.out.println("=========운영자 메뉴=========");
			System.out.println("1.회원 관리 2.문의사항 관리 0.종료");
			System.out.println("==========================");
			System.out.print("입력>> ");
			try {
				menuM = Integer.parseInt(scn.nextLine());
				if (menuM > 2) {
					System.out.println("메뉴에 맞는 번호를 입력해 주세요.");
					continue;
				}
			} catch (Exception e) {
				System.out.println("메뉴에 맞는 번호를 입력해 주세요.");
				continue;
			}

//			회원관리
			if (menuM == 1) {
				int subM = 0;
				System.out.println("1.회원목록조회 2.보호처리 수정 3.보유코인 수정 0.돌아가기");
				System.out.print("입력>> ");
				try {
					subM = Integer.parseInt(scn.nextLine());
					if (subM > 3) {
						System.out.println("메뉴에 맞는 번호를 입력해 주세요.");
						continue;
					}
				} catch (Exception e) {
					System.out.println("메뉴에 맞는 번호를 입력해 주세요.");
					continue;
				}
//				회원목록조회
				if (subM == 1) {
					int subM1 = 0;
					System.out.println("1.전체조회 2.상세조회");
					System.out.print("입력>> ");
					try {
						subM1 = Integer.parseInt(scn.nextLine());
						if (subM1 > 2) {
							System.out.println("메뉴에 맞는 번호를 입력해 주세요.");
							continue;
						}
					} catch (Exception e) {
						System.out.println("메뉴에 맞는 번호를 입력해 주세요.");
						continue;
					}
//					전체조회
					if (subM1 == 1) {
						List<Members> list = pdao.getList("userAll", "none");
						System.out.println("==================================================");
						System.out.println("아이디      이름   전화번호          메일주소");
						System.out.println("--------------------------------------------------");
						for (Members mem : list) {
							System.out.printf("%-10s", mem.getId());
							System.out.printf("%-5s", mem.getName());
							System.out.printf("%-15s", mem.getPhone());
							System.out.printf("%-20s", mem.getMail());
							System.out.println();
						}
						System.out.println("==================================================");
						System.out.println("조회 완료");
//					상세조회
					} else if (subM1 == 2) {
						String ckId = "";
						while (true) {
							System.out.println("조회할 ID를 입력해 주세요.");
							System.out.print("입력>> ");
							ckId = scn.nextLine();

							if (pdao.idCheck(ckId)) {
								List<Members> list = pdao.getList("user", ckId);
								System.out.println(
										"==========================================================================================");
								System.out.println("아이디      이름   전화번호          "//
										+ "메일주소               가입일        "//
										+ "아이디상태   보유코인  로그인실패횟수");
								System.out.println(
										"------------------------------------------------------------------------------------------");
								for (Members mem : list) {
									System.out.printf("%-10s", mem.getId());
									System.out.printf("%-5s", mem.getName());
									System.out.printf("%-15s", mem.getPhone());
									System.out.printf("%-20s", mem.getMail());
									String date = mem.getJoinDate();
									System.out.printf("%-12s", date.substring(0, 11));
									System.out.printf("%-11s", mem.getStatus());
									System.out.printf("%-10d", mem.getCoin());
									System.out.printf("%-2d", mem.getLoginFail());
									System.out.println();
								}
								System.out.println(
										"==========================================================================================");
								System.out.println("조회 완료");
								break;
							} else {
								System.out.println("존재하지 않는 ID입니다.");
								break;
							}
						}
					}
//				보호처리 수정
				} else if (subM == 2) {
					String cKid = "";
					while (true) {
						System.out.println("수정할 회원의 ID를 입력해주세요.");
						System.out.print("입력>> ");
						cKid = scn.nextLine();

						if (pdao.idCheck(cKid)) {
							List<Members> list = pdao.getList("user", cKid);
							for (Members mem : list) {
								System.out.printf("[ %s 회원님의 현재 계정상태는 %s입니다. ]", cKid, mem.getStatus());
								System.out.println();
							}
							System.out.println("1.계정 활성화 2.계정 보호처리");
							System.out.print("입력>> ");
							int num = 0;
							try {
								num = Integer.parseInt(scn.nextLine());
								if (num > 2 && num > 0) {
									System.out.println("메뉴에 맞는 번호를 입력해 주세요.");
									continue;
								}
							} catch (Exception e) {
								System.out.println("메뉴에 맞는 번호를 입력해 주세요.");
								continue;
							}
							System.out.println(num);
							String stat = "";
							int fail = 0;
							if (num == 1) {
								stat = "activated";
							} else if (num == 2) {
								stat = "locked";
							}
							if (stat.equals("activated")) {
								fail = 0;
							} else if (stat.equals("locked")) {
								fail = 5;
							}
							Members mem = new Members();
							mem.setId(cKid);
							mem.setStatus(stat);
							mem.setLoginFail(fail);
							if (pdao.updateMem(mem, "manager")) {
								System.out.println("수정 완료");
								break;
							} else {
								System.out.println("수정중 에러발생");
							}
						} else {
							System.out.println("존재하지 않는 ID입니다.");
							break;
						}
					}
//				보유코인 수정
				} else if (subM == 3) {
					String cKid = "";
					while (true) {
						System.out.println("수정할 회원의 ID를 입력해주세요.");
						System.out.print("입력>> ");
						cKid = scn.nextLine();

						if (pdao.idCheck(cKid)) {
							List<Members> list = pdao.getList("user", cKid);
							for (Members mem : list) {
								System.out.printf("[ %s 회원님의 현재 보유코인량은 %s코인입니다. ]", cKid, mem.getCoin());
								System.out.println();
							}
							System.out.println("변경할 코인값을 입력해주세요.");
							System.out.print("입력>> ");
							int num = scn.nextInt();
							scn.nextLine();

							Members mem = new Members();
							mem.setId(cKid);
							mem.setCoin(num);

							if (pdao.updateMem(mem, "sys")) {
								System.out.println("수정 완료");
								break;
							} else {
								System.out.println("수정중 에러발생");
							}
						} else {
							System.out.println("존재하지 않는 ID입니다.");
							break;
						}
					}
				}
//			문의사항 처리
			} else if (menuM == 2) {
				int page = 1;
				String inNo = "";
				while (true) {
					System.out.println("===============================================================");
					System.out.println("번호   분류   작성자       제목                     처리상황  작성일자");
					System.out.println("---------------------------------------------------------------");
					List<Boards> list = pdao.getBoard("all", "manager", page);
					for (Boards bod : list) {
						String date = bod.getWrite_date();
						System.out.printf(" %-3s", bod.getRownum());
						System.out.printf("%-6s", bod.getHead());
						System.out.printf("%-10s", bod.getId());
						System.out.printf("%-22s", bod.getTitle());
						String ck = "";
						if (bod.getAnswer_check().equals("X")) {
							ck = "처리중";
						} else if (bod.getAnswer_check().equals("O")) {
							ck = "답변완료";
						}
						System.out.printf("%-5s", ck);
						System.out.printf("%-12s", date.substring(0, 11));
						System.out.println();
					}
					System.out.println("===============================================================");
					int totalCnt = pdao.getTotalCnt("");
					int lastPage = (int) Math.ceil(totalCnt / 5.0);
					System.out.print(" <<");
					for (int i = 1; i <= lastPage; i++) {
						System.out.printf("%3d", i);
					}
					System.out.println(" >> ");
					int loMenu = 0;
					System.out.println("1.페이지이동 2.내용조회 3.답변 작성/수정 4.삭제 0.돌아가기");
					System.out.print("입력>> ");
					loMenu = Integer.parseInt(scn.nextLine());
					try {
						if (loMenu > 4 && loMenu < 0) {
							System.out.println("메뉴에 맞는 번호를 입력해 주세요.");
							continue;
						}
					} catch (Exception e) {
						System.out.println("메뉴에 맞는 번호를 입력해 주세요.");
						continue;
					}
					if (loMenu == 1) {// 페이지이동
						System.out.print("페이지>> ");
						page = Integer.parseInt(scn.nextLine());
					} else if (loMenu == 2) {// 내용조회
						System.out.println("조회할 글번호를 입력해주세요");
						System.out.print("입력>> ");
						page = Integer.parseInt(scn.nextLine());
						System.out.println("======================================================");
						System.out.println("번호   분류   제목                    작성일자       작성시간");
						System.out.println("------------------------------------------------------");
						List<Boards> getList = pdao.getBoard("search", "manager", page);
						for (Boards bod : getList) {
							String date = bod.getWrite_date();
							System.out.printf(" %-3s", bod.getRownum());
							System.out.printf("%-5s", bod.getHead());
							System.out.printf("%-22s", bod.getTitle());
							System.out.printf("%-12s", date.substring(0, 11));
							System.out.printf("%-12s", date.substring(11));
							System.out.println();
							System.out.println("-[내용]--------------------------------------------------");
							System.out.printf(" %s", bod.getMain_text());
							System.out.println();
							System.out.println();
							System.out.println("-[답변]--------------------------------------------------");
							String ans = "";
//							ans = bod.getAnswer();
							if (bod.getAnswer_check().equals("X")) {
								ans = " 처리중입니다. ";
							} else if (bod.getAnswer_check().equals("O")) {
								ans = bod.getAnswer();
							}
							System.out.printf("%s", ans);
							System.out.println();
							System.out.println();
						}
						System.out.println("======================================================");
						break;
					} else if (loMenu == 3) {// 수정
						System.out.println("답변을 작성/수정할 글번호를 입력해주세요");
						System.out.print("입력>> ");
						int no = Integer.parseInt(scn.nextLine()) - 1;
						String pick = list.get(no).getCode();
						System.out.println("작성/수정할 답변을 입력해주세요");
						System.out.print("입력>> ");
						String ans = scn.nextLine();

						Boards bod = new Boards();
						bod.setAnswer(ans);

						if (pdao.updateBod("manager", bod, pick)) {
							System.out.println("수정 성공");
						} else {
							System.out.println("오류 발생");
						}
					} else if (loMenu == 4) {// 삭제
						System.out.println("삭제할 글번호를 입력해주세요");
						System.out.print("입력>> ");
						int no = Integer.parseInt(scn.nextLine()) - 1;
						String pick = list.get(no).getCode();
						Boards bod = new Boards();
						bod.setCode(pick);
						if (pdao.deleteBod(bod)) {
							System.out.println("삭제 성공");
						} else {
							System.out.println("삭제중 오류발생");
						}
					} else if (loMenu == 0) {// 돌아가기
						break;
					}
				}
			} else if (menuM == 0) {
				System.out.println("종료합니다.");
				manaC = false;
			}
		} // end of managerMenu
//		관리자화면
		while (sysC) {
			System.out.println("관리자 화면입니다.");
			int menuS = 0;
			System.out.println("=========관리자 메뉴=========");
			System.out.println("1.운영자 관리 2.BOT 관리 0.종료");
			System.out.println("==========================");
			System.out.print("입력>> ");
			try {
				menuS = Integer.parseInt(scn.nextLine());
				if (menuS > 2) {
					System.out.println("메뉴에 맞는 번호를 입력해 주세요.");
					continue;
				}
			} catch (Exception e) {
				System.out.println("메뉴에 맞는 번호를 입력해 주세요.");
				continue;
			}
			if (menuS == 1) {// 운영자 관리
				int subMe = 0;
				System.out.println("1.운영자 등록 2.운영자 목록조회 3.운영자 정보수정 4.운영자 삭제 0.돌아가기");
				System.out.print("입력>> ");
				try {
					subMe = Integer.parseInt(scn.nextLine());
					if (subMe > 4 || subMe < 0) {
						System.out.println("메뉴에 맞는 번호를 입력해 주세요.");
						continue;
					}
				} catch (Exception e) {
					System.out.println("메뉴에 맞는 번호를 입력해 주세요.");
					continue;
				}
				if (subMe == 1) {// 운영자 등록
					boolean nameCk = true;
					boolean idCk = true;
					boolean pwCk = true;
					String name = "";
					String user_id = "";
					String user_pw = "";
					String user_mail = "";
					String user_phone = "";
					while (nameCk) {
						System.out.print("이름>> ");
						name = scn.nextLine();
						if (name.equals("")) {
							System.out.println("이름에 공백을 사용할 수 없습니다.");
							continue;
						}
						nameCk = false;
					}
					while (idCk) {
						System.out.print("아이디>> ");
						user_id = scn.nextLine();
						if (user_id.equals("")) {
							System.out.println("아이디에 공백을 사용할 수 없습니다.");
							continue;
						} else if (pdao.idCheck(user_id)) {
							System.out.println("이미 등록된 아이디입니다.");
							continue;
						}
						idCk = false;
					}
					while (pwCk) {
						System.out.print("비밀번호>> ");
						user_pw = scn.nextLine();
						if (user_pw.equals("")) {
							System.out.println("비밀번호에 공백을 사용할 수 없습니다.");
							continue;
						}
						pwCk = false;
					}
					System.out.print("메일주소>> ");
					user_mail = scn.nextLine();
					System.out.print("전화번호>> ");
					user_phone = scn.nextLine();

					Members uMem = new Members(null, name, user_id, user_pw, user_mail, user_phone, null, null, 0, 0);

					if (pdao.insertMem(uMem, "sys")) {
						System.out.println("정상적으로 등록.");
						pdao.BoardNo("f", user_id);
						pdao.insertRecord(user_id);
					} else {
						System.out.println("등록 에러.");
					}
				} else if (subMe == 2) {// 운영자 목록조회
					int subM1 = 0;
					System.out.println("1.전체조회 2.상세조회");
					System.out.print("입력>> ");
					try {
						subM1 = Integer.parseInt(scn.nextLine());
						if (subM1 > 2) {
							System.out.println("메뉴에 맞는 번호를 입력해 주세요.");
							continue;
						}
					} catch (Exception e) {
						System.out.println("메뉴에 맞는 번호를 입력해 주세요.");
						continue;
					}
//					전체조회
					if (subM1 == 1) {
						List<Members> list = pdao.getList("managerAll", "none");
						System.out.println("==================================================");
						System.out.println("아이디      이름   전화번호          메일주소");
						System.out.println("--------------------------------------------------");
						for (Members mem : list) {
							System.out.printf("%-10s", mem.getId());
							System.out.printf("%-5s", mem.getName());
							System.out.printf("%-15s", mem.getPhone());
							System.out.printf("%-20s", mem.getMail());
							System.out.println();
						}
						System.out.println("==================================================");
						System.out.println("조회 완료");
//					상세조회
					} else if (subM1 == 2) {
						String ckId = "";
						while (true) {
							System.out.println("조회할 ID를 입력해 주세요.");
							System.out.print("입력>> ");
							ckId = scn.nextLine();

							if (pdao.idCheck(ckId)) {
								List<Members> list = pdao.getList("manager", ckId);
								System.out.println(
										"====================================================================================================");
								System.out.println("아이디      비밀번호     이름   전화번호          "//
										+ "메일주소               가입일        "//
										+ "아이디상태   보유코인  로그인실패횟수");
								System.out.println(
										"----------------------------------------------------------------------------------------------------");
								for (Members mem : list) {
									System.out.printf("%-10s", mem.getId());
									System.out.printf("%-10s", mem.getPw());
									System.out.printf("%-5s", mem.getName());
									System.out.printf("%-15s", mem.getPhone());
									System.out.printf("%-20s", mem.getMail());
									String date = mem.getJoinDate();
									System.out.printf("%-12s", date.substring(0, 11));
									System.out.printf("%-11s", mem.getStatus());
									System.out.printf("%-10d", mem.getCoin());
									System.out.printf("%-2d", mem.getLoginFail());
									System.out.println();
								}
								System.out.println(
										"====================================================================================================");
								System.out.println("조회 완료");
								break;
							} else {
								System.out.println("존재하지 않는 ID입니다.");
								break;
							}
						}
					}
				} else if (subMe == 3) {// 운영자 정보수정
					System.out.println("공백 입력시 해당항목은 수정되지 않습니다.");
					System.out.println("수정할 운영자 ID를 입력해주세요.");
					System.out.print("입력>> ");
					String id = scn.nextLine();
					System.out.println("새로 사용할 비밀번호를 입력해주세요.");
					System.out.print("입력>> ");
					String pw = scn.nextLine();
					System.out.println("수정할 전화번호를 입력해주세요.");
					System.out.print("입력>> ");
					String ph = scn.nextLine();
					System.out.println("수정할 메일주소를 입력해주세요.");
					System.out.print("입력>> ");
					String em = scn.nextLine();
					Members mem = new Members();
					mem.setId(id);
					mem.setPw(pw);
					mem.setPhone(ph);
					mem.setMail(em);
					if (pdao.updateMem(mem, "user")) {
						System.out.println("수정 완료");
					} else {
						System.out.println("수정중 오류 발생");
					}
				} else if (subMe == 4) {// 운영자 삭제
					System.out.println("삭제할 운영자 ID를 입력해주세요");
					System.out.print("입력>> ");
					String id = scn.nextLine();
					System.out.println("정말로 삭제 하시겠습니까?(y/n)");
					System.out.print("입력>> ");
					String ckk = scn.nextLine();
					if (ckk.equals("y")) {
						Members mem = new Members();
						mem.setId(id);
						if (pdao.deleteMem(mem)) {
							System.out.println("삭제 완료");
						} else {
							System.out.println("삭제중 오류발생");
						}
					} else if (ckk.equals("n")) {
						continue;
					} else {
						System.out.println("'y' 또는 'n'을 입력해주세요");
					}
				} else if (subMe == 0) {//
					continue;
				}
			} else if (menuS == 2) {// 봇 관리
				System.out.println("미완성");
				int subMe = 0;
				System.out.println("1.BOT 등록 2.BOT 목록조회 3.BOT 정보수정 4.BOT 전략조회 0.돌아가기");
				System.out.print("입력>> ");
				try {
					subMe = Integer.parseInt(scn.nextLine());
					if (subMe > 4 || subMe < 0) {
						System.out.println("메뉴에 맞는 번호를 입력해 주세요.");
						continue;
					}
				} catch (Exception e) {
					System.out.println("메뉴에 맞는 번호를 입력해 주세요.");
					continue;
				}
//				봇등록
				if (subMe == 1) {
					System.out.println("BOT 번호를 입력해주세요");
					System.out.print("입력>>  ");
					int no = 0;
					System.out.println("BOT 이름를 입력해주세요");
					System.out.print("입력>>  ");
					String name = "";
					System.out.println("BOT 전략번호를 입력해주세요");
					System.out.print("입력>>  ");
					int tac_no = 0;
					Bots bot = new Bots(no, name, 0, 0, tac_no);

					if (pdao.insertBot(bot)) {
						System.out.println("정상적으로 등록.");
					} else {
						System.out.println("등록 에러.");
					}
//				봇조회
				} else if (subMe == 2) {
					List<Bots> list = pdao.getBot("check");
					System.out.println("========================");
					System.out.println(" BOT번호 BOT이름 전략번호");
					System.out.println("------------------------");
					for (Bots bot : list) {
						System.out.printf(" %-5d", bot.getBot_no());
						System.out.printf("%-10s", bot.getBot_name());
						System.out.printf("%-5d", bot.getTac_no());
						System.out.println();
					}
					System.out.println("=========================");
					System.out.println("조회 완료");

//				봇수정
				} else if (subMe == 3) {
					System.out.println("수정할 BOT 번호를 입력해주세요");
					System.out.print("입력>>  ");
					int no = 0;
					System.out.println("수정할 BOT 이름를 입력해주세요");
					System.out.print("입력>>  ");
					String name = "";
					System.out.println("수정할 BOT 전략번호를 입력해주세요");
					System.out.print("입력>>  ");
					int tac_no = 0;
					Bots bot = new Bots(no, name, 0, 0, tac_no);
					if (pdao.updateBot(bot)) {
						System.out.println("정상적으로 수정.");
					} else {
						System.out.println("수정 중 에러.");
					}
//				전술조회
				} else if (subMe == 4) {
					List<Tactics> list = pdao.getTactic();
					System.out.println("====================================================");
					System.out.println(" 전략번호  내용");
					System.out.println("----------------------------------------------------");
					for (Tactics tac : list) {
						System.out.printf("   %-5d", tac.getNo());
						System.out.printf("%s", tac.getText());
						System.out.println();
					}
					System.out.println("=====================================================");
					System.out.println("조회 완료");
				} else if (subMe == 0) {

				}

			} else if (menuS == 0) {
				System.out.println("종료합니다.");
				sysC = false;
			}
		} // end of sysMenu
	} // end of main
}
