//--------table format creation and connect with mysql------
package servlet_crud;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
@Data

@Entity
public class Employee {
	@Id
//	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@GeneratedValue
	private int id;
	private String Name;
	private String email;
	private long MobileNO;
	private int salary;
	
	
	

}
//--------insert operation--------------

package servlet_crud;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/insert")
public class InsertServlet  extends HttpServlet{
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String name=req.getParameter("name");
		String email=req.getParameter("email");
		long mobile=Long.parseLong(req.getParameter("mobile"));
		int salary=Integer.parseInt(req.getParameter("salary"));
		
		Employee employee=new Employee();
		employee.setName(name);
		employee.setEmail(email);
		employee.setMobileNO(mobile);
		employee.setSalary(salary);
		
		new DatabaseLogic().save(employee);
		
		resp.getWriter().print("<h1 align='center'>Records saved Successfully</h1>");
		req.getRequestDispatcher("index.html").include(req, resp);
	}

}
//-------Fetch the records---------

package servlet_crud;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/fetch")
public class Fetch_Records extends HttpServlet{
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		EntityManagerFactory factory=Persistence.createEntityManagerFactory("E3");
		EntityManager manager=factory.createEntityManager();
		
		List<Employee> list=manager.createNativeQuery("select * from employee",Employee.class).getResultList();
		req.setAttribute("records", list);
		req.getRequestDispatcher("fetch.jsp").include(req, resp);
	}

}
//---------Edit the records--------
package servlet_crud;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/edit")
public class Edit_Records  extends HttpServlet{
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int id=Integer.parseInt(req.getParameter("id"));
		
		EntityManagerFactory factory=Persistence.createEntityManagerFactory("E3");
		EntityManager manager=factory.createEntityManager();
		EntityTransaction transaction=manager.getTransaction();
		 
		Employee employee=manager.find(Employee.class, id);
		req.setAttribute("emp", employee);
		req.getRequestDispatcher("edit.jsp").forward(req, resp);
	}
	

}
//-------delete the records--------
package servlet_crud;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/delete")
public class Delete_Records extends HttpServlet {
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int id=Integer.parseInt(req.getParameter("id"));
		
		
		new DatabaseLogic().delete(id);
		resp.getWriter().print("<h1 align='center'>Record Deleted succesfully</h1>");
		req.getRequestDispatcher("fetch").include(req, resp);
	}

}
//----------update operation---------
package servlet_crud;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/update")
public class Update_Records  extends HttpServlet{
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		String name=req.getParameter("name");
		String email=req.getParameter("email");
		long mobile=Long.parseLong(req.getParameter("mobile"));
		int salary=Integer.parseInt(req.getParameter("salary"));
		String id=req.getParameter("id");
		
		Employee employee=new Employee();
		employee.setName(name);
		employee.setEmail(email);
		employee.setMobileNO(mobile);
		employee.setSalary(salary);
		employee.setId(Integer.parseInt(id));
		
		new DatabaseLogic().update(employee);
		
		resp.getWriter().print("<h1 align='center'>Record updated succesfully</h1>");
		req.getRequestDispatcher("fetch").include(req, resp);
	}

}

//---------Database logic to all crudoperation----------
package servlet_crud;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class DatabaseLogic {
	EntityManagerFactory factory=Persistence.createEntityManagerFactory("E3");
	EntityManager manager=factory.createEntityManager();
	EntityTransaction transaction=manager.getTransaction();
	 
	public void save(Employee employee) {
	transaction.begin();
	manager.persist(employee);
	transaction.commit();
	}
	
	public void delete(int id) {
Employee employee=manager.find(Employee.class, id);
		
		transaction.begin();
		manager.remove(employee);
		transaction.commit();
	}
	public void update(Employee employee) {
		transaction.begin();
		manager.merge(employee);
		transaction.commit();
		
		
	}
	
	public Employee find(int id) {
		return manager.find(Employee.class, id);
		
		
	}

	

}






