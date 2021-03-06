package se.kth.grupp12.recruitool.model.pdf;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import se.kth.grupp12.recruitool.model.Application;
import se.kth.grupp12.recruitool.model.Availability;
import se.kth.grupp12.recruitool.model.CompetenceProfile;

public class ApplicationPDF implements AutoCloseable {
	
	private PDDocument document;
	
	/**
	 * Creates a PDF document from the application object.
	 * @param appl
	 * @throws IOException 
	 */
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
	
	/**
	 * Saves this document to an output stream.
	 * @param out
	 * @throws IOException 
	 */
	public void save(OutputStream out) throws IOException {
		try {
			document.save(out);
		} catch (COSVisitorException ex) {
			Logger.getLogger(ApplicationPDF.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	/**
	 * Close this document.
	 * @throws IOException 
	 */
	@Override
	public void close() throws IOException {
		document.close();
	}
}
