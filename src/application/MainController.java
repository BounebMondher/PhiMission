package application;

import com.jfoenix.controls.JFXPasswordField;
import java.util.Date;
import com.jfoenix.controls.JFXTextArea;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

public class MainController {
	@FXML
	private Text lblStatus;
	@FXML
	private JFXTextField username;
	@FXML
	private JFXTextField Nom;
	@FXML
	private JFXTextField Prenom;
	@FXML
	private JFXTextField Email;
	@FXML
	private JFXPasswordField password;
	static String nom;
	static String prenom;
	static String email;
	@FXML
	private Text user;
	@FXML private Label alert;
	@FXML
	private JFXDatePicker date_aller;
	@FXML
	private JFXDatePicker date_retour;
	@FXML
	private JFXTextField ville;
	@FXML
	private JFXTextField pays;
	@FXML
	private JFXTextArea motif;
	@FXML
	private JFXTextArea commentaire;
	
	
	ObservableList<String> trans = FXCollections.observableArrayList("Avion","Train","Bateau","Voiture","Spaceship","Autre");
	@FXML
	private JFXComboBox transport;
	@FXML
	private void initialize()
	{
		if (transport!=null)
		{transport.setValue("Voiture");
		transport.setItems(trans);}
		if(user!=null)
			user.setText(nom+" "+prenom);
		if (Email!=null)
			Email.setText(email);
		if (Nom!=null)
			Nom.setText(nom);
		if (Prenom!=null)
			Prenom.setText(prenom);
	}
	
	public void login(ActionEvent event) throws Exception
	{
		
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://localhost:3306/Phi","root",""); 
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select * from users where login='"+username.getText()+"'");
		if (rs.next()&&rs.getString(3).equals(password.getText())&&rs.getString(4).equals("demandeur"))
		{
			nom=rs.getString(5);
			prenom=rs.getString(6);
			email=rs.getString(2);
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			rs=stmt.executeQuery("select * from demandes where email='"+email+"' and etat='en attente' and date_aller>'"+dateFormat.format(date)+"'");
			if(!rs.next())
			{
			((Node)(event.getSource())).getScene().getWindow().hide();
			Stage primaryStage=new Stage();
			Parent root = FXMLLoader.load(getClass().getResource("/application/Main.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Demande de mission");
			primaryStage.show();
			}
			else
			{
				((Node)(event.getSource())).getScene().getWindow().hide();
				Stage primaryStage=new Stage();
				Parent root = FXMLLoader.load(getClass().getResource("/application/Suivi.fxml"));
				Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				primaryStage.setScene(scene);
				primaryStage.setTitle("Suivi de mission");
				primaryStage.show();
			}
		}
		rs.beforeFirst();
		if (rs.next()&&rs.getString(3).equals(password.getText())&&rs.getString(4).equals("validateur"))
		{
			nom=rs.getString(5);
			prenom=rs.getString(6);
			email=rs.getString(2);
			((Node)(event.getSource())).getScene().getWindow().hide();
			Stage primaryStage=new Stage();
			Parent root = FXMLLoader.load(getClass().getResource("/application/admin.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Validation des missions");
			primaryStage.show();
		}
		else
			lblStatus.setText("Identifiant ou mot de passe incorrecte! Veuillez réessayer");
	
	
	}catch(Exception e){ System.out.println(e);
	}  
	}
	
	public void redir (ActionEvent event) throws Exception
    {
    	try {
    		((Node)(event.getSource())).getScene().getWindow().hide();
    		Stage primaryStage=new Stage();
			Parent root = FXMLLoader.load(getClass().getResource("/application/Demande.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Demander une mission");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
    }
	
	public void redir2 (ActionEvent event) throws Exception
    {
		
		Class.forName("com.mysql.jdbc.Driver");  
		Connection con=DriverManager.getConnection(  
		"jdbc:mysql://localhost:3306/Phi","root",""); 
		Statement stmt=con.createStatement(); 
		DatePicker a=date_aller;
		DatePicker r=date_retour;
		ComboBox c=transport;
		if( ville.getText().isEmpty() || pays.getText().isEmpty()||motif.getText().isEmpty()||commentaire.getText().isEmpty())
			alert.setText("Veuillez renseigner les champs vides!");
		else
		{
		if (stmt.executeUpdate("insert into demandes values('"+Nom.getText()+"','"+Prenom.getText()+"','"+Email.getText()+"','"+a.getValue().toString()+"','"+r.getValue().toString()+"','"+ville.getText()+"','"+pays.getText()+"','"+c.getValue().toString()+"','"+motif.getText()+"','"+commentaire.getText()+"','en attente')")>0)
		{try {
    		((Node)(event.getSource())).getScene().getWindow().hide();
    		Stage primaryStage=new Stage();
			Parent root = FXMLLoader.load(getClass().getResource("/application/Suivi.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Suivi du mission");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}}
		}
    }
	
	public void annuler(ActionEvent event) throws Exception
	{
		Class.forName("com.mysql.jdbc.Driver");  
		Connection con=DriverManager.getConnection(  
		"jdbc:mysql://localhost:3306/Phi","root",""); 
		Statement stmt=con.createStatement();
		stmt.executeUpdate("delete from demandes where email='"+email+"' and etat='en attente'");
		((Node)(event.getSource())).getScene().getWindow().hide();
		Stage primaryStage=new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/application/Main.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setTitle("Demande de mission");
		primaryStage.show();
	}

}
