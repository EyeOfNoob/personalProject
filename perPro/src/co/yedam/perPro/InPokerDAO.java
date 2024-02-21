package co.yedam.perPro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import co.yedam.perPro.DAO;

// DB 처리기능 구현
public class InPokerDAO {
// 필드
	Connection conn;
	PreparedStatement psmt;
	ResultSet rs;
	String sql;

// 사용이 끝난 메모리 초기화 기능
	void disconn() {
		try {
			if (conn != null) {
				conn.close();
			}
			if (psmt != null) {
				psmt.close();
			}
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}// end of disconn

//	로그인용 객체 생성
	public int loginMem(String id, String pw) {
		conn = DAO.getConn();
		sql = "SELECT id_status "//
				+ "FROM members "//
				+ "WHERE id = ? ";
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			rs = psmt.executeQuery();
			if (rs.next()) {
				if (rs.getString("id_status").equals("locked")) {
					return -3;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		sql = "SELECT    password,"//
				+ "      loginfail "//
				+ "FROM  members "//
				+ "WHERE id = ? ";

		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			rs = psmt.executeQuery();
			if (rs.next()) {
				if (rs.getString("password").equals(pw)) {
					return 1; // 로그인 성공
				} else {
					sql = "UPDATE    members "//
							+ "SET   loginfail = (SELECT loginfail " //
							+ "                   FROM   members"//
							+ "                   WHERE  id = ? )+1 "//
							+ "WHERE id = ? ";
					psmt = conn.prepareStatement(sql);
					psmt.setString(1, id);
					psmt.setString(2, id);
					psmt.execute();

					sql = "SELECT    loginfail "//
							+ "FROM  members "//
							+ "WHERE id = ? ";
					psmt = conn.prepareStatement(sql);
					psmt.setString(1, id);
					rs = psmt.executeQuery();
					if (rs.next()) {
						int error = rs.getInt("loginfail");
						if (error == 5) {
							sql = "UPDATE    members "//
									+ "SET   id_status = 'locked' "//
									+ "WHERE id = ? ";
							psmt = conn.prepareStatement(sql);
							psmt.setString(1, id);
							rs = psmt.executeQuery();
							return -4;
						}
						System.out.println("로그인 " + error + "회 실패, 5회 실패시 계정이 보호처리됩니다.");
					}
					return 0; // 비밀번호 틀림
				}
			}
			return -1; // 없는 아이디
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			disconn();
		}
		return -2; // DB에러
	}

//	로그인한 유저 객체 생성
	public Members whoMem(String id, String pw) {
		conn = DAO.getConn();
		Members mem = new Members();
		sql = "UPDATE members "//
				+ "SET loginfail = 0 "//
				+ "WHERE id = ? ";
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			rs = psmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		sql = "SELECT type,"//
				+ "   name,"//
				+ "   id,"//
				+ "   password,"//
				+ "   phone,"//
				+ "   mail,"//
				+ "   join_date,"//
				+ "   id_status,"//
				+ "   coin, "//
				+ "   loginFail "//
				+ "FROM members "//
				+ "WHERE id = ? "//
				+ "AND password = ? ";
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			psmt.setString(2, pw);
			rs = psmt.executeQuery();
			while (rs.next()) {
				mem = new Members();
				mem.setType(rs.getString("type"));
				mem.setName(rs.getString("name"));
				mem.setId(rs.getString("id"));
				mem.setPw(rs.getString("password"));
				mem.setPhone(rs.getString("phone"));
				mem.setMail(rs.getString("mail"));
				mem.setJoinDate(rs.getString("join_date"));
				mem.setStatus(rs.getString("id_status"));
				mem.setCoin(rs.getInt("coin"));
				mem.setLoginFail(rs.getInt("loginFail"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconn();
		}

		return mem;
	}

//	회원 등록(일반유저/운영자 구별가능하게)
	public boolean insertMem(Members mem, String whois) {
		conn = DAO.getConn();
		String type = "";
		String typeText = "";
		String coin = "";
		String coinText = "";
		if (whois.equals("user")) {
			type = "";
			typeText = "";
			coin = ", coin";
			coinText = ", 500";
		} else if (whois.equals("sys")) {
			type = "type, ";
			typeText = "'manager', ";
			coin = "";
			coinText = "";
		} else {
			return false;
		}

		sql = "INSERT INTO members (" + type//
				+ "                 name,"//
				+ "                 id,"//
				+ "                 password,"//
				+ "                 phone,"//
				+ "                 mail"//
				+ "                 " + coin + ") " //
				+ "VALUES(" + typeText //
				+ "       ?, "//
				+ "       ?, "//
				+ "       ?, "//
				+ "       ?, "//
				+ "       ? "//
				+ "       " + coinText + ")";

		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, mem.getName());
			psmt.setString(2, mem.getId());
			psmt.setString(3, mem.getPw());
			psmt.setString(4, mem.getPhone());
			psmt.setString(5, mem.getMail());
			int r = psmt.executeUpdate();
			if (r > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconn();
		}

		return false;
	} // end of insert.

//	아이디 중복처리
	public boolean idCheck(String id) {
		conn = DAO.getConn();
		sql = "SELECT    id "//
				+ "FROM  members "//
				+ "WHERE id = ? ";
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			rs = psmt.executeQuery();
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconn();
		}
		return false;
	}

//	회원정보 수정(일반유저용/운영자용/관리자용 구별가능하게)
//		일반유저	: 자신의 비밀번호/전화번호/메일
//		운영자 	: 유저의 아이디상태
//		관리자 	: 운영자의 비밀번호/전화번호/메일,
//				  유저의 아이디상태/코인보유
	public boolean updateMem(Members mem, String whois) {
		conn = DAO.getConn();

		String setText = "";
		if (whois.equals("user")) {
//			setText = "password = nvl(?, password), "//
//					+ "phone = nvl(?, phone), "//
//					+ "mail = nvl(?, mail) ";
			if(!mem.getPw().equals("")) {
				setText += "password = ? , ";
			}else {
				setText += "password = password , ";
			}
			if(!mem.getPhone().equals("")) {
				setText += "phone = ? , ";
			}else {
				setText += "phone = phone , ";
			}
			if(!mem.getMail().equals("")) {
				setText += "mail = ? ";
			}else {
				setText += "mail = mail ";
			}
		} else if (whois.equals("manager")) {
			setText = "id_status = ?, " //
					+ "loginfail = ? ";
		} else if (whois.equals("sys")) {
			setText = "coin = ?";
		} else {
			return false;
		}

		sql = "UPDATE members " //
				+ "SET " + setText //
				+ "WHERE id = ? ";
		int p = 1;
		try {
			psmt = conn.prepareStatement(sql);
			if (whois.equals("user")) {
				if(!mem.getPw().equals("")) {
					System.out.println("비번");
					psmt.setString(p, mem.getPw());
					p += 1;
				}
				if(!mem.getPhone().equals("")) {
					System.out.println(p);
					System.out.println("전화번호");
					psmt.setString(p, mem.getPhone());
					p += 1;
					System.out.println(p);
				}
				if(!mem.getMail().equals("")) {
					System.out.println("메일");
					psmt.setString(p, mem.getMail());
					p += 1;
				}
				System.out.println(p);
			} else if (whois.equals("manager")) {
				psmt.setString(1, mem.getStatus());
				psmt.setInt(2, mem.getLoginFail());
				p += 2;
			} else if (whois.equals("sys")) {
				psmt.setInt(1, mem.getCoin());
				p += 1;
			}
			psmt.setString(p, mem.getId());
			int r = psmt.executeUpdate();
			if (r > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconn();
		}
		return false;
	}// end of update

//	게임 기능
//	셔플덱 생성
	public List<Deck> shuffleDeck() {
		conn = DAO.getConn();
		List<Deck> list = new ArrayList<>();
		sql = "SELECT card_no,"//
				+ "   card "//
				+ "FROM cards ";
		try {
			psmt = conn.prepareStatement(sql);
			rs = psmt.executeQuery();
			while (rs.next()) {
				Deck deck = new Deck();
				deck.setCard_no(rs.getInt("card_no"));
				deck.setCard(rs.getString("card"));
				list.add(deck);
			}
			Collections.shuffle(list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

//	메이드 체크
	public String ckMade(String card1, String card2) {
		String made = "";

		int type1 = 0;
		int no1 = 0;
		int type2 = 0;
		int no2 = 0;

		if (card1.substring(0, 1).equals("♠")) {
			type1 = 40;
		} else if (card1.substring(0, 1).equals("◆")) {
			type1 = 30;
		} else if (card1.substring(0, 1).equals("♥")) {
			type1 = 20;
		} else if (card1.substring(0, 1).equals("♣")) {
			type1 = 10;
		}

		if (card2.substring(0, 1).equals("♠")) {
			type2 = 40;
		} else if (card2.substring(0, 1).equals("◆")) {
			type2 = 30;
		} else if (card2.substring(0, 1).equals("♥")) {
			type2 = 20;
		} else if (card2.substring(0, 1).equals("♣")) {
			type2 = 10;
		}

		if (card1.substring(1).equals("A")) {
			no1 = 14;
		} else if (card1.substring(1).equals("J")) {
			no1 = 11;
		} else if (card1.substring(1).equals("Q")) {
			no1 = 12;
		} else if (card1.substring(1).equals("K")) {
			no1 = 13;
		} else {
			no1 = Integer.parseInt(card1.substring(1, 2));
		}

		if (card2.substring(1).equals("A")) {
			no2 = 14;
		} else if (card2.substring(1).equals("J")) {
			no2 = 11;
		} else if (card2.substring(1).equals("Q")) {
			no2 = 12;
		} else if (card2.substring(1).equals("K")) {
			no2 = 13;
		} else {
			no2 = Integer.parseInt(card2.substring(1, 2));
		}

		int score = 0;

		if (type1 == type2) {
			if ((no1 == 14 && no2 == 13) || (no2 == 14 && no1 == 13)) {
//					로열 스트레이트 플러시
				made = "로열 스트레이트 플러시";
			} else if ((no1 == 14 && no2 == 2) || (no2 == 14 && no1 == 2)) {
//					로열 백스트레이트 플러시
				made = "로열 백스트레이트 플러시";
			} else if ((no1 - no2 == 1) || (no1 - no2 == -1)) {
//					스트레이트 플러시
				made = "스트레이트 플러시";
			} else {
//					플러시
				made = "플러시";
			}
		} else if (type1 != type2) {
			if ((no1 == 14 && no2 == 13) || (no2 == 14 && no1 == 13)) {
//					로열 스트레이트
				made = "로열 스트레이트";
			} else if ((no1 == 14 && no2 == 2) || (no2 == 14 && no1 == 2)) {
//					로열 백스트레이트
				made = "로열 백스트레이트";
			} else if ((no1 - no2 == 1) || (no1 - no2 == -1)) {
//					스트레이트
				made = "스트레이트";
			} else if (no1 == no2) {
//					페어
				made = "페어";
			} else {
//					탑카드
				String a = Integer.toString((int) Math.max(no1, no2));
//				String check = Integer.toString(aa.getss);
				String a1 = Integer.toString(no1);
				String a2 = Integer.toString(no2);

				String top = "";
				if (a.equals(a1)) {
					top = card1;
				} else if (a.equals(a2)) {
					top = card2;
				}
				made = top + "탑";
			}
		}
		return made;
	}

//	점수처리
	public int ckScore(String card1, String card2) {
		int type1 = 0;
		int no1 = 0;
		int type2 = 0;
		int no2 = 0;

		if (card1.substring(0, 1).equals("♠")) {
			type1 = 40;
		} else if (card1.substring(0, 1).equals("◆")) {
			type1 = 30;
		} else if (card1.substring(0, 1).equals("♥")) {
			type1 = 20;
		} else if (card1.substring(0, 1).equals("♣")) {
			type1 = 10;
		}

		if (card2.substring(0, 1).equals("♠")) {
			type2 = 40;
		} else if (card2.substring(0, 1).equals("◆")) {
			type2 = 30;
		} else if (card2.substring(0, 1).equals("♥")) {
			type2 = 20;
		} else if (card2.substring(0, 1).equals("♣")) {
			type2 = 10;
		}

		if (card1.substring(1).equals("A")) {
			no1 = 14;
		} else if (card1.substring(1).equals("J")) {
			no1 = 11;
		} else if (card1.substring(1).equals("Q")) {
			no1 = 12;
		} else if (card1.substring(1).equals("K")) {
			no1 = 13;
		} else {
			no1 = Integer.parseInt(card1.substring(1, 2));
		}

		if (card2.substring(1).equals("A")) {
			no2 = 14;
		} else if (card2.substring(1).equals("J")) {
			no2 = 11;
		} else if (card2.substring(1).equals("Q")) {
			no2 = 12;
		} else if (card2.substring(1).equals("K")) {
			no2 = 13;
		} else {
			no2 = Integer.parseInt(card2.substring(1, 2));
		}

		int score = 0;

		if (type1 == type2) {
			if ((no1 == 14 && no2 == 13) || (no2 == 14 && no1 == 13)) {
//				로열 스트레이트 플러시
				score = 8000;
				score += (int) Math.max(no1, no2) * 10;
				score += (int) Math.max(type1, type2);
//				System.out.println("로열 스트레이트 플러시 메이드!");
			} else if ((no1 == 14 && no2 == 2) || (no2 == 14 && no1 == 2)) {
//				로열 백스트레이트 플러시
				score = 7000;
				score += (int) Math.max(no1, no2) * 10;
				score += (int) Math.max(type1, type2);
//				System.out.println("로열 백스트레이트 플러시 메이드!");
			} else if ((no1 - no2 == 1) || (no1 - no2 == -1)) {
//				스트레이트 플러시
				score = 6000;
				score += (int) Math.max(no1, no2) * 10;
				score += (int) Math.max(type1, type2);
//				System.out.println("스트레이트 플러시 메이드!");
			} else {
//				플러시
				score = 1000;
				score += (int) Math.max(no1, no2) * 10;
				score += (int) Math.max(type1, type2);
//				System.out.println("플러시 메이드!");
			}
		} else if (type1 != type2) {
			if ((no1 == 14 && no2 == 13) || (no2 == 14 && no1 == 13)) {
//				로열 스트레이트
				score = 4000;
				score += (int) Math.max(no1, no2) * 10;
				score += (int) Math.max(type1, type2);
//				System.out.println("로열 스트레이트 메이드!");
			} else if ((no1 == 14 && no2 == 2) || (no2 == 14 && no1 == 2)) {
//				로열 백스트레이트
				score = 3000;
				score += (int) Math.max(no1, no2) * 10;
				score += (int) Math.max(type1, type2);
//				System.out.println("로열 백스트레이트 메이드!");
			} else if ((no1 - no2 == 1) || (no1 - no2 == -1)) {
//				스트레이트
				score = 2000;
				score += (int) Math.max(no1, no2) * 10;
				score += (int) Math.max(type1, type2);
//				System.out.println("스트레이트 메이드!");
			} else if (no1 == no2) {
//				페어
				score = 5000;
				score += (int) Math.max(no1, no2) * 10;
				score += (int) Math.max(type1, type2);
//				System.out.println("페어 메이드!");
			} else {
//				탑카드
				score = (int) Math.max(no1, no2) * 10;
				score += (int) Math.max(type1, type2);
				String a = Integer.toString((int) Math.max(no1, no2));
				String a1 = Integer.toString(no1);
				String a2 = Integer.toString(no2);

//				String top = "";
//				if (a.equals(a1)) {
//					top = card1;
//				} else if (a.equals(a2)) {
//					top = card2;
//				}
//				System.out.println(top + "탑카드");
			}
		}
		return score;
	}

//	봇 전략작동1턴 - 플러시가 아닌경우 선택?
	public String botTactic1(int no, String card) {
		int card_no = 0;
		if (card.substring(1).equals("A")) {
			card_no = 14;
		} else if (card.substring(1).equals("J")) {
			card_no = 11;
		} else if (card.substring(1).equals("Q")) {
			card_no = 12;
		} else if (card.substring(1).equals("K")) {
			card_no = 13;
		} else {
			card_no = Integer.parseInt(card.substring(1));
		}
		if (no == 1) {
			if (card_no > 3) {
				return "베팅";
			} else {
				return "폴드";
			}
		} else if (no == 2) {
			if (card_no > 7) {
				return "베팅";
			} else {
				return "폴드";
			}
		} else if (no == 3) {
			return "베팅";
		} else if (no == 4) {
			return "베팅";
		}
		return "";
	}

//	봇 전략작동2턴
	public String botTactic2(int no, String card1, String card2) {
		int chP = ckScore(card1, card2);

		if (no == 1) {
			if (chP > 2000) {
				return "베팅";
			} else {
				return "폴드";
			}
		} else if (no == 2) {
			if (chP > 4000) {
				return "베팅";
			} else {
				return "폴드";
			}
		} else if (no == 3) {
			if (chP > 6000) {
				return "베팅";
			} else {
				return "폴드";
			}
		} else if (no == 4) {
			return "베팅";
		}
		return "";
	}
//	게임 설명
	public String guide() {
		String a = " InPoker는 조커카드를 제외한 총 52장 카드 중 2장을 사용하여 진행하는 미니포커입니다.\r\n"//
				+ " 게임의 기본 베팅액은 10코인이며 무작위로 선정된 BOT과 대결하게됩니다.또한 항상 유저 선턴으로 진행됩니다.\r\n"//
				+ " BOT의 베팅금액은 항상 유저의 베팅금액과 동일하며 베팅여부는 BOT이 뽑은 카드에 의해 결정됩니다.\r\n"//
				+ " 승패판단은 족보에 의해 결정되며 족보를 만들지 못했을경우 가장 높은 카드 1장으로 점수를 비교하게 됩니다.\r\n"//
				+ " 족보가 동일하거나 카드숫자가 동일한 경우에는 '♠ > ◆ > ♥ > ♣'로 점수를 판단하게 됩니다.\r\n"//
				+ " 재미있게 즐겨주세요.";
		return a;
	}
//	족보 조회
	public List<Made> madeGuide() {
		conn = DAO.getConn();
		List<Made> list = new ArrayList<>();
		sql = "SELECT   made_no, "//
				+ "     name, "//
				+ "     score, "//
				+ "     guide "//
				+ "FROM made ";
		try {
			psmt = conn.prepareStatement(sql);
			rs = psmt.executeQuery();
			while(rs.next()) {
				Made mad = new Made();
				mad.setMade_no(rs.getInt("made_no"));
				mad.setName(rs.getString("name"));
				mad.setScore(rs.getInt("score"));
				mad.setGuide(rs.getString("guide"));
				list.add(mad);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconn();
		}
		return list;
	}

//	전적 관리
//	전적 생성 - 일반 유저가 가입시 자동생성
	public void insertRecord(String id) {
		conn = DAO.getConn();
		sql = "INSERT INTO Record (id) "//
				+ "VALUES         (?) ";

		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			psmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconn();
		}
	}

//	전적 수정-승패마다 자동 수정
	public void updateRcd(String id, int botNo, String wl) {
		conn = DAO.getConn();

		sql = "UPDATE record ";
		if (wl.equals("win")) {
			sql += "SET bot"+botNo+"_win = bot"+botNo+"_win + 1, " //
					+ "total_win = total_win + 1 ";
		}

		if (wl.equals("lose")) {
			sql += "SET bot"+botNo+"_lose = bot"+botNo+"_lose + 1, " //
					+ "total_lose = total_lose + 1 ";
		}

		sql += "WHERE id = ? ";

		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			psmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconn();
		}

	}

//	전적 조회
	public List<Records> getRecord(String id) {
		conn = DAO.getConn();
		List<Records> list = new ArrayList<>();
		sql = "SELECT   id,"
				+ "     bot1_win,"//
				+ "     bot1_lose,"//
				+ "     bot2_win,"//
				+ "     bot2_lose,"//
				+ "     bot3_win,"//
				+ "     bot3_lose,"//
				+ "     total_win,"//
				+ "     total_lose "//
				+ "FROM record "
				+ "WHERE id = ?";
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			rs = psmt.executeQuery();
			while(rs.next()) {
				Records rcd = new Records();
				rcd.setId(rs.getString("id"));
				rcd.setBot1_win(rs.getInt("bot1_win"));
				rcd.setBot1_lose(rs.getInt("bot1_lose"));
				rcd.setBot2_win(rs.getInt("bot2_win"));
				rcd.setBot2_lose(rs.getInt("bot2_lose"));
				rcd.setBot3_win(rs.getInt("bot3_win"));
				rcd.setBot3_lose(rs.getInt("bot3_lose"));
				rcd.setTotal_win(rs.getInt("total_win"));
				rcd.setTotal_lose(rs.getInt("total_lose"));
				list.add(rcd);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconn();
		}
		return list;
	}
//	문의사항 등록
	public boolean insertBoard(Boards bod) {
		conn = DAO.getConn();
		String answer = "";
		String answerText = "";

		sql = "INSERT INTO board (division_code, "//
				+ "               id, "//
				+ "               head, "//
				+ "               title, "//
				+ "               main_text "//
				+ "               " + answer + " ) "//
				+ "VALUES(?, "//
				+ "       ?, "//
				+ "       ?, "//
				+ "       ?, "//
				+ "       ? "//
				+ "       " + answerText + " ) ";

		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, bod.getCode());
			psmt.setString(2, bod.getId());
			psmt.setString(3, bod.getHead());
			psmt.setString(4, bod.getTitle());
			psmt.setString(5, bod.getMain_text());

			int r = psmt.executeUpdate();
			if (r > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconn();
		}

		return false;
	}

//	문의사항 유저별 카운팅
	public int BoardNo(String opt, String id) {
		conn = DAO.getConn();
		int no = 0;
		if (opt.equals("f")) {// 최초 가입시 생성
			sql = "INSERT INTO last_board_no (id) "//
					+ "VALUES (?) ";
		} else if (opt.equals("0")) {// 게시글 작성시 참고조회
			sql = "SELECT    last_no "//
					+ "FROM  last_board_no "//
					+ "WHERE id = ? ";
		} else if (opt.equals("+")) {// 작성 성공시 증가
			sql = "UPDATE last_board_no "//
					+ "SET   last_no = last_no + 1 " + "WHERE id = ? ";
		}
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			if (opt.equals("f")) {
				psmt.executeUpdate();
			} else if (opt.equals("0")) {
				rs = psmt.executeQuery();
				while (rs.next()) {
					no = rs.getInt("last_no");
				}
			} else if (opt.equals("+")) {
				psmt.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconn();
		}

		return no;
	}

//	문의사항 조회
//	일반유저 - 자신의 게시글 전체/상세조회
//	운영자  - 전체조회/상세조회
	public List<Boards> getBoard(String mode, String who, int page) {
		conn = DAO.getConn();
		List<Boards> list = new ArrayList<>();
		String lis = "";
		String lis2 = "";

		if (mode.equals("userAll")) {
			lis = "WHERE id = ? ";
			lis2 = "b.rn BETWEEN (? * 5) - 4 AND (?)*5  ";
		} else if (mode.equals("user")) {
			lis = "WHERE id = ? ";
			lis2 = "b.rn = ? ";
		} else if (mode.equals("search")) {
			lis2 = "b.rn = ? ";
		} else if (mode.equals("all")) {
			lis2 = "b.rn BETWEEN (? * 5) - 4 AND (?)*5 ";
		}

		sql = "SELECT   b.rn, "//
				+ "     division_code,"//
				+ "     id, "//
				+ "     head, "//
				+ "     title, "//
				+ "     main_text, "//
				+ "     answer_check, "//
				+ "     answer, "//
				+ "     write_date "//
				+ "FROM (SELECT ROWNUM rn, a.* "//
				+ "      FROM (SELECT * "//
				+ "            FROM board "//
				+ "           " + lis//
				+ "            ORDER BY write_date ) a ) b "//
				+ "WHERE " + lis2;
		try {
			psmt = conn.prepareStatement(sql);
			if (mode.equals("userAll")) {
				psmt.setString(1, who);
				psmt.setInt(2, page);
				psmt.setInt(3, page);
			} else if (mode.equals("user")) {
				psmt.setString(1, who);
				psmt.setInt(2, page);
			} else if (mode.equals("search")) {
				psmt.setInt(1, page);
			} else if (mode.equals("all")) {
				psmt.setInt(1, page);
				psmt.setInt(2, page);
			}

			rs = psmt.executeQuery();

			while (rs.next()) {
				Boards bod = new Boards();
				bod.setRownum(rs.getString("rn"));
				bod.setCode(rs.getString("division_code"));
				bod.setId(rs.getString("id"));
				bod.setHead(rs.getString("head"));
				bod.setTitle(rs.getString("title"));
				bod.setMain_text(rs.getString("main_text"));
				bod.setAnswer_check(rs.getString("answer_check"));
				bod.setAnswer(rs.getString("answer"));
				bod.setWrite_date(rs.getString("write_date"));
				list.add(bod);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconn();
		}
		return list;
	}

//  문의사항 카운팅
	public int getTotalCnt(String who) {
		conn = DAO.getConn();
		sql = "SELECT count(*) AS cnt "//
				+ "FROM board " + "WHERE id = nvl(?, id) ";
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, who);
			rs = psmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconn();
		}
		return -1;
	}

//	문의사항/답변 수정
	public boolean updateBod(String who, Boards bod, String code) {
		conn = DAO.getConn();
		if (who.equals("user")) {
			sql = "UPDATE board "//
					+ "SET title = ?, "//
					+ "    main_text = ? "//
					+ "WHERE division_code = ? ";
		} else if (who.equals("manager")) {
			sql = "UPDATE    board " //
					+ "SET   answer_check = 'O', "//
					+ "      answer = ? "//
					+ "WHERE division_code = ? ";
		}
		try {
			psmt = conn.prepareStatement(sql);
			int p = 2;
			if (who.equals("user")) {
				psmt.setString(1, bod.getTitle());
				psmt.setString(2, bod.getMain_text());
				p += 1;
			} else if (who.equals("manager")) {
				psmt.setString(1, bod.getAnswer());
			}
			psmt.setString(p, code);
			int r = psmt.executeUpdate();
			if (r > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconn();
		}
		return false;
	}

//	문의사항 삭제
	public boolean deleteBod(Boards bod) {
		conn = DAO.getConn();
		sql = "DELETE FROM board " //
				+ "WHERE  division_code = ? " + "CASCADE ";
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, bod.getCode());
			int r = psmt.executeUpdate();
			if (r > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconn();
		}

		return false;
	}

//	회원목록 조회 -(일반유저/운영자 구별 가능하게)전체조회/단건조회
	public List<Members> getList(String mode, String who) {
		conn = DAO.getConn();
		List<Members> list = new ArrayList<>();
		String lis = "";
		if (mode.equals("userAll")) {// 유저 전체조회
			lis = "WHERE type = 'user'";
		} else if (mode.equals("user")) {// 유저 조건조회
			lis = "WHERE type = 'user' AND id = ? ";
		} else if (mode.equals("managerAll")) {// 운영자 전체조회
			lis = "WHERE type = 'manager'";
		} else if (mode.equals("manager")) {// 운영자 조건조회
			lis = "WHERE type = 'manager' AND id = ? ";
		} else if (mode.equals("noMode")) {// 전체 조회
			lis = " ";
		}

		sql = "SELECT    name, "//
				+ "      id, "
				+ "      password, "//
				+ "      phone, "//
				+ "      mail, "//
				+ "      join_date, "//
				+ "      id_status, "//
				+ "      coin, "//
				+ "      loginfail "//
				+ "FROM  members "//
				+ lis;
		try {
			psmt = conn.prepareStatement(sql);

			if (mode.equals("user") || mode.equals("manager")) {
				psmt.setString(1, who);
			}
			rs = psmt.executeQuery();
			while (rs.next()) {
				Members mem = new Members();
				mem.setName(rs.getString("name"));
				mem.setId(rs.getString("id"));
				mem.setPw(rs.getString("password"));
//				if (rs.getString("phone").length() < 13) {전화번호 가운데번호 3/4자리 길이통일.
//					mem.setPhone(rs.getString("phone") + " ");
//				} else {
				mem.setPhone(rs.getString("phone"));
//				}
				mem.setMail(rs.getString("mail"));
				mem.setJoinDate(rs.getString("join_date"));
				mem.setStatus(rs.getString("id_status"));
				mem.setCoin(rs.getInt("coin"));
				mem.setLoginFail(rs.getInt("loginfail"));
				list.add(mem);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconn();
		}

		return list;
	}

//	회원 탈퇴(삭제)
	public boolean deleteMem(Members mem) {
		conn = DAO.getConn();
		sql = "DELETE FROM members " //
				+ "WHERE   id = ? ";
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, mem.getId());
			int r = psmt.executeUpdate();
			if (r > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconn();
		}
		return false;
	}

//	봇 등록
	public boolean insertBot(Bots bot) {
		conn = DAO.getConn();
		sql = "INSERT INTO bots (bot_no, "//
				+ "              name, "//
				+ "              tactics_no) "//
				+ "VALUES(?,"//
				+ "       ?,"//
				+ "       ?)";

		try {
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, bot.getBot_no());
			psmt.setString(2, bot.getBot_name());
			psmt.setInt(3, bot.getTac_no());

			int r = psmt.executeUpdate();
			if (r > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconn();
		}
		return false;
	}

//	봇 전체수 조회
	public int getTotalBot() {
		conn = DAO.getConn();
		sql = "SELECT count(*) AS cnt "//
				+ "FROM bots ";
		try {
			psmt = conn.prepareStatement(sql);
			rs = psmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconn();
		}
		return -1;
	}

//	봇 조회 -전체조회/단일조회
	public List<Bots> getBot(String mode) {
		int no = getTotalBot();
		int ranNo = (int) (Math.random() * no + 1);
		conn = DAO.getConn();
		List<Bots> list = new ArrayList<>();
		String opt = "";
		if (mode.equals("game")) {
			opt = "WHERE bot_no = ? ";
		} else if (mode.equals("check")) {

		}
		sql = "SELECT bot_no, "//
				+ "   name, "//
				+ "   win, "//
				+ "   lose, "//
				+ "   tactics_no "//
				+ "FROM bots " + opt;

		try {
			psmt = conn.prepareStatement(sql);
			if (mode.equals("game")) {
				psmt.setInt(1, ranNo);
			} else if (mode.equals("check")) {

			}
			rs = psmt.executeQuery();
			while (rs.next()) {
				Bots bot = new Bots();
				bot.setBot_no(rs.getInt("bot_no"));
				bot.setBot_name(rs.getString("name"));
				bot.setWin(rs.getInt("win"));
				bot.setLose(rs.getInt("lose"));
				bot.setTac_no(rs.getInt("tactics_no"));
				list.add(bot);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconn();
		}
		return list;
	}

//	봇 정보 수정
	public boolean updateBot(Bots bot) {
		conn = DAO.getConn();
		sql = "UPDATE bots "//
				+ "SET name = ?, "//
				+ "    tactics_no = ? "//
				+ "WHERE bot_no = ? ";
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, bot.getBot_name());
			psmt.setInt(2, bot.getTac_no());
			psmt.setInt(3, bot.getBot_no());
			int r = psmt.executeUpdate();
			if (r > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconn();
		}
		return false;
	}
//	봇 삭제
//	전술목록 조회
	public List<Tactics> getTactic() {
		conn = DAO.getConn();
		List<Tactics> list = new ArrayList<>();
		
		sql = "SELECT tactics_no, "//
				+ "   text "//
				+ "FROM tactics ";

		try {
			psmt = conn.prepareStatement(sql);
			
			rs = psmt.executeQuery();
			while (rs.next()) {
				Tactics tac = new Tactics();
				tac.setNo(rs.getInt("tactics_no"));
				tac.setText(rs.getString("text"));
				list.add(tac);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconn();
		}
		return list;
	}
//	인사말 및 공지사항 수정

}// end of InPokerDAO
