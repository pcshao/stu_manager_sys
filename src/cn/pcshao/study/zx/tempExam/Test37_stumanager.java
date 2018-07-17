package cn.pcshao.study.zx.tempExam;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import cn.pcshao.study.zx.util.DBHelper;
import cn.pcshao.study.zx.util.ScannerUtil;
import cn.pcshao.study.zx.util.UI;

/**
 * 学生信息管理系统
 * @author pcshao
 *
 */
public class Test37_stumanager {

	static int privil = 0;
	static Connection conn = null;
	static User nowUser;
	
	public static void main(String[] args) {
		//欢迎
		System.out.println("欢迎登陆学生信息管理系统");
		//登录失败计数
		int loginCount=1;
		while(true) {
			try {
				if((privil=login())!=0) {
					System.out.println("登陆成功");break;
				}
				System.out.println("登陆失败，请检查用户名和密码");
				//错误三次，退出系统
				if(loginCount++>=3) System.exit(0);
			} catch (SQLException e) {
			}
		}
		//
		while(true) {
			try {
				UI.welcome(new String[] {"用户信息管理","学生信息管理","成绩信息管理","退出系统"},false);
				//用户信息管理、学生信息管理、成绩信息管理
				int input = ScannerUtil.getIntNumber();
				switch(input) {
					case 1:	UI.welcome(new String[] {"添加用户","修改密码","移除用户","查看用户信息","返回上一级"},true);break;
					case 2:	UI.welcome(new String[] {"学生 查看所有学生信息","查看学生姓名模糊查询","添加学生","修改学生","移除学生","返回上一级"},true);break;
					case 3:	UI.welcome(new String[] {"查看所有","查看学生姓名模糊","添加学生成绩","修改学生成绩","移除学生成绩","返回上一级"},true);break;
					case 4: conn.close();System.exit(1);
					default:System.out.println("错误输入");
				}
				//功能选择
				if(input==1) {
					switch(ScannerUtil.getIntNumber()) {
						case 1:addUser();break;
						case 2:updatePassw();break;
						case 3:removeUser();break;
						case 4:showUsers();break;
						case 5:System.out.println("返回上级");break;
							default:System.out.println("输入错误");
					}
				}
				if(input==2) {
					switch(ScannerUtil.getIntNumber()) {
						case 1:getAllStus();break;
						case 2:getStu();break;
						case 3:addStu();break;
						case 4:editStu();break;
						case 5:removeStu();break;
						case 6:System.out.println("返回上级");break;
						default:System.out.println("输入错误");
					}
				}
				if(input==3) {
					switch(ScannerUtil.getIntNumber()) {
					case 1:showAllScore();break;
					case 2:findScoreByName();break;
					case 3:addScore();break;
					case 4:editScore();break;
					case 5:removeScore();break;
					case 6:System.out.println("返回上一级");break;
					default:System.out.println("输入错误");
					}
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
			//成绩 查看所有、查看学生姓名模糊、添加学生成绩、修改学生成绩、移除学生成绩、返回上一级
	}
	//*************************************************************
	private static void removeScore() throws SQLException {
		showAllScore();
		System.out.println("输入要删除的那一条成绩的ssid");
		String ssid = ScannerUtil.getString();
		String sql = "delete from stu_score where ssid = "+ssid;
		executeDB(sql);
		System.out.println("完成");
	}

	private static void editScore() throws SQLException {
		System.out.println("编辑学生成绩信息，请输入要编辑的那条成绩的编号ssid");
		showAllScore();
		int ssid = ScannerUtil.getIntNumber();
		System.out.println("请输入更改后的成绩");
		int score = ScannerUtil.getIntNumber();
		String sql = "update stu_score set(score="+score+")where ssid="+ssid+";";
		executeDB(sql);
		System.out.println("完成");
	}

	
	private static void addScore() throws SQLException {
		System.out.println("请输入要添加的学生成绩信息,可用课程信息如下");
		getSubject();
		System.out.println("请输入学生编号：");
		String sid = ScannerUtil.getString();
		System.out.println("请输入科类名称：");
		String subject = ScannerUtil.getString();
		System.out.println("请输入"+subject+"成绩：");
		int score= ScannerUtil.getIntNumber();
		//DAO
		String sql = "insert into stu_score VALUES(stu_score_ssid.nextval,'"+subject+"',"+score+","+sid+")";
		executeDB(sql);
		System.out.println("完成");
	}

	private static void findScoreByName() throws SQLException {
		System.out.println("请输入要查询的学生ID *(优化完多表级联查询后添加姓名查询)");
		String name = ScannerUtil.getString();
		getScore(name);
	}

	private static void showAllScore() throws SQLException{
		ArrayList<Score> scores = new ArrayList<Score>();
		String sql = "select b.s_id,s.ssid,b.name,s.subject,s.score from stu_score s inner join stu_base b on s.s_id=b.s_id";
		ResultSet rs = executeQueryDB(sql);
		while(rs.next()) {
			Score score = new Score();
			score.setName(rs.getString("name"));
			score.setS_id(rs.getInt("s_id"));
			score.setSubject(rs.getString("subject"));
			score.setScore(rs.getInt("score"));
			score.setSsid(rs.getInt("ssid"));
			scores.add(score);
		}
		String lastName = "";
		for(Score s :scores) {
			if(lastName.equals(s.getName())) {
				System.out.println(s.getSsid()+","+s.getSubject()+","+s.getScore());
			}else {
				System.out.println("************");
				System.out.println(s.getName()+","+s.getS_id());
				System.out.println(s.getSsid()+","+s.getSubject()+","+s.getScore());
				lastName=s.getName();
			}
		}
	}
	//*************************************************************
	private static void getScore(String name) throws SQLException {
		String sql = "select b.s_id,s.ssid,b.name,s.subject,s.score from stu_score s inner join stu_base b on s.s_id=b.s_id "
				+ "and b.name like '%"+name+"%'";
		ResultSet rs = executeQueryDB(sql);
		if(rs.next()) {
			Score s = new Score();
			s.setName(rs.getString("name"));
			s.setS_id(rs.getInt("s_id"));
			s.setSubject(rs.getString("subject"));
			s.setScore(rs.getInt("score"));
			s.setSsid(rs.getInt("ssid"));
			System.out.println(s.getName()+","+s.getS_id());
			System.out.println(s.getSsid()+","+s.getSubject()+","+s.getScore());
		}
	}
	private static void getSubject() throws SQLException{
		ArrayList<Subject> subjects = new ArrayList<Subject>();
		String sql = "select * from subject";
		ResultSet rs = executeQueryDB(sql);
		while(rs.next()) {
			Subject subject = new Subject();
			subject.setName(rs.getString("name"));
			subject.setTeacher(rs.getString("teacher"));
			subjects.add(subject);
		}
		for(Subject sub : subjects)
			System.out.println(sub);
	}
//*************************************************************
	private static void removeStu() throws SQLException {
		//找到记录后确认删除
		getStu();		
		System.out.println("请输入要删除的学生ID");
		String id = ScannerUtil.getString();
		String sql = "delete from stu_base where name="+id+"";
		executeDB(sql);
	}

	private static void editStu() throws SQLException{
		getStu();
		System.out.println("请输入要编辑的ID");
		String id = ScannerUtil.getString();
		System.out.println("请输入要编辑的学生信息(格式：\"id,name,sex,age\")");
		String[] stu = null;
		while(true) {
			String input = ScannerUtil.getString();
			stu = input.split(",");
			if(stu.length==4)
				break;
		}
		String sql = "update stu_base set s_id="+stu[0]+",name='"+stu[1]+"',sex='"+
			stu[2]+"',age="+stu[3]+" where s_id = "+id+"";
		executeDB(sql);
		System.out.println(sql);
	}

	private static void addStu() throws SQLException{
		System.out.println("请输入要添加的学生(格式：\"id,name,sex,age\"");
		String input = ScannerUtil.getString();
		String[] stu = input.split(",");
		String sql = "insert into stu_base values("+stu[0]+",'"+stu[1]+"','"+
			stu[2]+"',"+stu[3]+")";
		executeDB(sql);
		System.out.println(sql);
	}

	private static void getStu() throws SQLException{
		System.out.println("请输入要查找的学生姓名（自动补全）");
		String name = ScannerUtil.getString();
		String sql = "select * from stu_base where name like '%"+name+"%'";
		ResultSet rs = executeQueryDB(sql);
		Stu_base stu = null;
		if(rs.next()) {
			stu = new Stu_base(rs.getInt("s_id"),rs.getString("name"),
				rs.getString("sex").charAt(0),rs.getInt("age"));
		}
		if(rs!=null)
			rs.close();
		System.out.println(stu);
	}

	private static void getAllStus() throws SQLException {
		//DAO
		ArrayList<Stu_base> students = new ArrayList<Stu_base>();
		String sql = "select * from stu_base";
		ResultSet rs = executeQueryDB(sql);
		while(rs.next()) {
			Stu_base stu = new Stu_base();
			stu.setS_id(rs.getInt("s_id"));
			stu.setName(rs.getString("name"));
			stu.setSex(rs.getString("sex").charAt(0));
			stu.setAge(rs.getInt("age"));
			students.add(stu);
		}
		if(rs!=null)
			rs.close();
		for(Stu_base stu:students)
			System.out.println(stu);
	}
	//*************************************************************
	/**
	 * 权限判断
	 * @return
	 */
	public static boolean isSuper() {
		if(privil==2) {
			System.out.println("非超级管理员，非礼勿视");
			return false;
		}
		return true;
	}
	
	private static void showUsers() throws SQLException {
		//权限判断
		if(!isSuper()) 
			return;
		//DAO
		ArrayList<User> users = new ArrayList<User>();
		String sql = "select * from users";
		ResultSet rs = executeQueryDB(sql);
		while(rs.next()) {
			User user = new User();
			user.setCount(rs.getString("count"));
			user.setPassword(rs.getString("password"));
			user.setPrivil(rs.getInt("privil"));
			users.add(user);
		}
		if(rs!=null) {
			rs.close();
		}
		for(User user:users)
			System.out.println(user);
	}

	private static void removeUser() throws SQLException {
		//权限判断
		if(!isSuper()) 
			return;
		//
		showUsers();
		System.out.println("请输入要删除的用户名：");
		String count = ScannerUtil.getString();
		//DAO
		String sql = "delete from users where count='"+count+"'";
		executeDB(sql);
		System.out.println("完成");
	}

	private static void updatePassw() throws SQLException {
		//
		String count = nowUser.getCount();
		String passw = nowUser.getPassword();//非必要
		int privil = nowUser.getPrivil();
		if(isSuper()) {
			showUsers();
			System.out.println("请输入要更改密码的用户名称：");
			count = ScannerUtil.getString();
			System.out.println("请输入密码：");
			passw = ScannerUtil.getString();
			System.out.println("请输入数字1表示即将添加超级管理员用户，输入数字2则表示普通用户");
			privil = ScannerUtil.getIntNumber();
		}else {
			System.out.println("请输入密码：");
			passw = ScannerUtil.getString();
		}
		//DAO
		String sql = "update users set password='"+passw+"',privil='"+privil+"' where count='"+count+"'";
		executeDB(sql);
		System.out.println("完成");
	}

	private static void addUser() throws SQLException {
		//权限判断
		if(!isSuper()) 
			return;
		//
		System.out.println("请输入要添加的用户名：");
		String count = ScannerUtil.getString();
		System.out.println("请输入要添加用户的密码：");
		String passw = ScannerUtil.getString();
		System.out.println("请输入数字1表示即将添加超级管理员用户，输入数字2则表示普通用户");
		int privil = ScannerUtil.getIntNumber();
		//DAO
		String sql = "insert into users (count,password,privil)values('"+count+"','"+passw+"','"+privil+"')";
		executeDB(sql);
		System.out.println("添加成功");
	}
	//*************************************************************
	public static void executeDB(String sql) throws SQLException{
		if(conn!=null)
			conn = DBHelper.getOracleConnection();
		conn.createStatement().execute(sql);
		System.out.println("操作完成");
	}
	
	public static ResultSet executeQueryDB(String sql) throws SQLException{
		if(conn!=null)
			conn = DBHelper.getOracleConnection();
		return conn.createStatement().executeQuery(sql);
	}
	//*************************************************************
	public static int login() throws SQLException {
		int ret=0;
		//登陆逻辑
		//用户名密码信息查表，成功则返回true
		System.out.println("请输入用户名");
		String count = ScannerUtil.getString();
		System.out.println("请输入密码");
		String passw = ScannerUtil.getString();
		System.out.println("登陆中...");
		//DAO
		conn = DBHelper.getOracleConnection();
		String sql = "select count,password,privil from users where count='"+count+"'and password='"+passw+"'";
		ResultSet rs = conn.createStatement().executeQuery(sql);
		if(rs.next()) {
			ret = rs.getInt("privil");
			nowUser = new User();
			nowUser.setCount(rs.getString("count"));
			nowUser.setPassword(rs.getString("password"));
			nowUser.setPrivil(rs.getInt("privil"));
		}
		if(rs!=null) {
			rs.close();
		}
		if(ret==0) {
			conn.close();
		}
		return ret;
	}
}
class Score{
	private int s_id;
	private int ssid;
	private String name;
	private String subject;
	private int score;
	public int getS_id() {
		return s_id;
	}
	public void setS_id(int s_id) {
		this.s_id = s_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getSsid() {
		return ssid;
	}
	public void setSsid(int ssid) {
		this.ssid = ssid;
	}
}
class User{
	private int id;
	private String count;
	private String password;
	private int privil;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getPrivil() {
		return privil;
	}
	public void setPrivil(int privil) {
		this.privil = privil;
	}
	@Override
	public String toString() {
		return "用户： [用户名=" + count + ", 密码=" + password + ", 权限=" + privil + "]";
	}
}
class Stu_base{
	
	private int s_id;
	private String name;
	private char sex;
	private int age;
	
	public Stu_base(int s_id, String name, char sex, int age) {
		this.s_id = s_id;
		this.name = name;
		this.sex = sex;
		this.age = age;
	}
	public Stu_base() {
	}
	public int getS_id() {
		return s_id;
	}
	public void setS_id(int s_id) {
		this.s_id = s_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public char getSex() {
		return sex;
	}
	public void setSex(char sex) {
		this.sex = sex;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	@Override
	public String toString() {
		return "学生： [s_id=" + s_id + ", name=" + name + ", sex=" + sex + ", age=" + age + "]";
	}
}
class Subject{
	private String name;
	private String teacher;
	public void setName(String name) {
		this.name = name;
	}
	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}
	@Override
	public String toString() {
		return "Subject [name=" + name + ", teacher=" + teacher + "]";
	}
}