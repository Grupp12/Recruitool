package model.pdf;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import model.Account;
import model.Application;
import model.Availability;
import model.CompetenceProfile;

public class ApplicationPDF implements AutoCloseable {
	
	public static void main(String[] args) throws ParseException, IOException {
		view.RegisterForm regForm = new view.RegisterForm();
		regForm.setFirstName("Jonathan");
		regForm.setLastName("Cohlin");
		regForm.setEmail("cohlin@kth.se");
		regForm.setUsername("cohlin");
		regForm.setPassword("abc123");
		Account acc = new Account(regForm);
		
		view.ApplicationForm applForm = new view.ApplicationForm();
		
		applForm.getAvailabilityForm().setFrom("2016-03-08");
		applForm.getAvailabilityForm().setTo("2016-04-08");
		applForm.submitAvailabilityForm();
		applForm.getAvailabilityForm().setFrom("2016-04-10");
		applForm.getAvailabilityForm().setTo("2016-05-10");
		applForm.submitAvailabilityForm();
		
		applForm.getCompetenceForm().setCompetence("Java");
		applForm.getCompetenceForm().setYearsOfExperience("-1");
		applForm.submitCompetenceForm();
		applForm.getCompetenceForm().setCompetence("C++");
		applForm.getCompetenceForm().setYearsOfExperience("9");
		applForm.submitCompetenceForm();
		
		acc.createApplication(applForm, null);
		
		ApplicationPDF applPdf = new ApplicationPDF(acc.getApplication());
		applPdf.save(new FileOutputStream("test_pdf.pdf"));
		applPdf.close();
	}
	
	private PDDocument document;
	
	public ApplicationPDF(Application appl) throws IOException {
		document = new PDDocument();
		
		PDPage page = new PDPage();
		document.addPage(page);
		
		PDFont subFont = PDType1Font.HELVETICA_BOLD;
		PDFont txtFont = PDType1Font.COURIER;
		
		try (PDPageContentStream contentStream = new PDPageContentStream(document, page))
		{
			contentStream.beginText();
			contentStream.moveTextPositionByAmount(60, 740);
			
			contentStream.setFont(subFont, 18);
			contentStream.drawString("Application");
			contentStream.setFont(txtFont, 12);
			contentStream.moveTextPositionByAmount(0, -20);
			contentStream.drawString("Registered: " + appl.getTimeOfRegistration());
			contentStream.moveTextPositionByAmount(0, -20);
			contentStream.drawString("Status: " + appl.getStatus().toString().toLowerCase());
			
			contentStream.moveTextPositionByAmount(0, -35);
			contentStream.setFont(subFont, 18);
			contentStream.drawString("Applicant");
			contentStream.setFont(txtFont, 12);
			contentStream.moveTextPositionByAmount(0, -20);
			contentStream.drawString("Name: " + appl.getAccount().getLastName() + ", " + appl.getAccount().getFirstName());
			contentStream.moveTextPositionByAmount(0, -20);
			contentStream.drawString("E-mail: " + appl.getAccount().getEmail());
			
			contentStream.moveTextPositionByAmount(0, -35);
			contentStream.setFont(subFont, 18);
			contentStream.drawString("Availability periods");
			contentStream.setFont(txtFont, 12);
			for (Availability avail : appl.getAvailabilities()) {
				contentStream.moveTextPositionByAmount(0, -20);
				contentStream.drawString(avail.getFrom() + " - " + avail.getTo());
			}
			
			contentStream.moveTextPositionByAmount(0, -35);
			contentStream.setFont(subFont, 18);
			contentStream.drawString("Competences");
			contentStream.setFont(txtFont, 12);
			for (CompetenceProfile comp : appl.getCompetences()) {
				contentStream.moveTextPositionByAmount(0, -20);
				contentStream.drawString(comp.getCompetence().getName() + ": " + comp.getYearsOfExperience() + " years");
			}
			
			contentStream.endText();
		}
	}
	
	public void save(OutputStream out) throws IOException {
		try {
			document.save(out);
		} catch (COSVisitorException ex) {
			Logger.getLogger(ApplicationPDF.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	@Override
	public void close() throws IOException {
		document.close();
	}
}
