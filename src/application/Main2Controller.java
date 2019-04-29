package application;



import java.io.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.internet.MimeMessage;



import java.net.URL;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXCheckBox;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main2Controller implements Initializable{

	@FXML private TableView<Demande> table;
	@FXML private TableColumn<Demande, String> nom;
	@FXML private TableColumn<Demande, String> prenom;
	@FXML private TableColumn<Demande, String> date_aller;
	@FXML private TableColumn<Demande, String> motif;
	@FXML private Text nbmission;
	@FXML private Text user;
	@FXML private AnchorPane pane1;
	@FXML private AnchorPane pane2;
	@FXML private Label nom1;
	@FXML private Label prenom1;
	@FXML private Label email;
	@FXML private Label date_aller1;
	@FXML private Label date_retour;
	@FXML private Label ville;
	@FXML private Label pays;
	@FXML private Label motif1;
	@FXML private Label commentaire;
	@FXML private Label transport;
	@FXML private Text alert;
	@FXML private JFXCheckBox budget;
	@FXML private JFXCheckBox staff;
	@FXML private JFXCheckBox motive;
	
	
	public ObservableList<Demande> list = FXCollections.observableArrayList();
	
	
	
	public void sendmail(boolean resp,String raison)
	{
		try{
            String host ="smtp.gmail.com" ;
            String user = "phimission2017@gmail.com";
            String pass = "helamour";
            String to = email.getText();
            String from = "phimission2017@gmail.com";
            String subject = "Reponse pour votre demande de mission";
            String messageText;
            if (resp)
             messageText= "Bonjour "+nom1.getText()+" "+prenom1.getText()+"\nVotre demande de mission a été accepté\nBien profitez!";
            else
            	messageText="Bonjour "+nom1.getText()+" "+prenom1.getText()+"\nMalheuresement votre demande de mission a été refusé pour les raisons suivantes :\n"+raison;
            boolean sessionDebug = false;

            Properties props = System.getProperties();

            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.required", "true");

            java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            Session mailSession = Session.getDefaultInstance(props, null);
            mailSession.setDebug(sessionDebug);
            Message msg = new MimeMessage(mailSession);
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] address = {new InternetAddress(to)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(subject); msg.setSentDate(new Date());
            msg.setText(messageText);

           Transport transport=mailSession.getTransport("smtp");
           transport.connect(host, user, pass);
           transport.sendMessage(msg, msg.getAllRecipients());
           transport.close();
        }catch(Exception ex)
        {
            System.out.println(ex);
        }

	}
	
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		table.setPlaceholder(new Label("Aucune mission"));
		user.setText(MainController.prenom+" "+MainController.nom);
		nom.setCellValueFactory(new PropertyValueFactory<Demande,String>("nom"));
		prenom.setCellValueFactory(new PropertyValueFactory<Demande,String>("prenom"));
		date_aller.setCellValueFactory(new PropertyValueFactory<Demande,String>("date_aller"));
		motif.setCellValueFactory(new PropertyValueFactory<Demande,String>("motif"));
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/Phi","root",""); 
			Statement stmt=con.createStatement(); 
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			ResultSet rs=stmt.executeQuery("select * from demandes where etat='en attente' and date_aller>'"+dateFormat.format(date)+"'");
			while(rs.next())
			{
				list.add(new Demande(rs.getString(1),rs.getString(2),rs.getString(4),rs.getString(9),rs.getString(3),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(10)));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		table.setItems(list);
		if (list.size()>0)
		nbmission.setText("Vous avez "+list.size()+" mission(s)");
		else
			nbmission.setText("Vous n'avez aucune mission");
	}
	
	public void explorer (ActionEvent event)
	{
		if(table.getSelectionModel().getSelectedItem()!=null)
		{
			Demande d= table.getSelectionModel().getSelectedItem();
			pane1.setVisible(false);
			pane2.setVisible(true);
			nom1.setText(d.getNom());
			prenom1.setText(d.getPrenom());
			email.setText(d.getEmail());
			date_aller1.setText(d.getDate_aller());
			date_retour.setText(d.getDate_retour());
			ville.setText(d.getVille());
			pays.setText(d.getPays());
			transport.setText(d.getTransport());
			motif1.setText(d.getMotif());
			commentaire.setText(d.getCommentaire());
		}
		
	}
	
	public void revenir (ActionEvent event)
	{
		pane2.setVisible(false);
		pane1.setVisible(true);
	}
	
	public void actualiser(ActionEvent event)
	{
		table.getItems().clear();
		table.setPlaceholder(new Label("Aucune mission"));
		user.setText(MainController.prenom+" "+MainController.nom);
		nom.setCellValueFactory(new PropertyValueFactory<Demande,String>("nom"));
		prenom.setCellValueFactory(new PropertyValueFactory<Demande,String>("prenom"));
		date_aller.setCellValueFactory(new PropertyValueFactory<Demande,String>("date_aller"));
		motif.setCellValueFactory(new PropertyValueFactory<Demande,String>("motif"));
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/Phi","root",""); 
			Statement stmt=con.createStatement(); 
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			ResultSet rs=stmt.executeQuery("select * from demandes where etat='en attente' and date_aller>'"+dateFormat.format(date)+"'");
			while(rs.next())
			{
				list.add(new Demande(rs.getString(1),rs.getString(2),rs.getString(4),rs.getString(9),rs.getString(3),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(10)));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		table.setItems(list);
		if (list.size()>0)
		nbmission.setText("Vous avez "+list.size()+" mission(s)");
		else
			nbmission.setText("Vous n'avez aucune mission");
	}
	
	
	public void actualise()
	{
		table.getItems().clear();
		table.setPlaceholder(new Label("Aucune mission"));
		user.setText(MainController.prenom+" "+MainController.nom);
		nom.setCellValueFactory(new PropertyValueFactory<Demande,String>("nom"));
		prenom.setCellValueFactory(new PropertyValueFactory<Demande,String>("prenom"));
		date_aller.setCellValueFactory(new PropertyValueFactory<Demande,String>("date_aller"));
		motif.setCellValueFactory(new PropertyValueFactory<Demande,String>("motif"));
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/Phi","root",""); 
			Statement stmt=con.createStatement(); 
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			ResultSet rs=stmt.executeQuery("select * from demandes where etat='en attente' and date_aller>'"+dateFormat.format(date)+"'");
			while(rs.next())
			{
				list.add(new Demande(rs.getString(1),rs.getString(2),rs.getString(4),rs.getString(9),rs.getString(3),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(10)));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		table.setItems(list);
		if (list.size()>0)
		nbmission.setText("Vous avez "+list.size()+" mission(s)");
		else
			nbmission.setText("Vous n'avez aucune mission");
	}
	
	
	public void refuser(ActionEvent event)
	{
		String raison="";
		if(!motive.isSelected()&&!budget.isSelected()&&!staff.isSelected())
			alert.setText("Veuillez spécifier au moin une raison!");
		else
		{
			if(motive.isSelected())
				raison+=motive.getText()+"\n";
			if(budget.isSelected())
				raison+=budget.getText()+"\n";
			if(staff.isSelected())
				raison+=staff.getText();
			sendmail(false,raison);
			pane2.setVisible(false);
			pane1.setVisible(true);
			try {
				annuler();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			actualise();
			}
	}
	
	public void accepter(ActionEvent event)
	{
		sendmail(true,"");
		pane2.setVisible(false);
		pane1.setVisible(true);
		try {
			annuler();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		actualise();
	}
	
	
	public void annuler() throws Exception
	{
		Class.forName("com.mysql.jdbc.Driver");  
		Connection con=DriverManager.getConnection(  
		"jdbc:mysql://localhost:3306/Phi","root",""); 
		Statement stmt=con.createStatement();
		stmt.executeUpdate("delete from demandes where email='"+email.getText()+"' and etat='en attente'");
	}
	
	
    

}
