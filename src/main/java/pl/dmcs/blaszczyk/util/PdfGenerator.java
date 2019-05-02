package pl.dmcs.blaszczyk.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import pl.dmcs.blaszczyk.model.Entity.Bill;
import pl.dmcs.blaszczyk.model.Exception.ServerException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class PdfGenerator {
    public static ByteArrayInputStream billReport(Bill bill) {
        if (bill == null) {
            return null;
        }
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, out);
            document.open();
            document.add(new Paragraph("RACHUNEK # " + String.valueOf(bill.getId())));
            document.add(new Paragraph("Dla okresu: " + String.valueOf(bill.getMeasurement().getMonth()) + "." + String.valueOf(bill.getMeasurement().getYear())));
            List orderedList = new List(List.ORDERED);
            orderedList.add(new ListItem("Prad: " + String.valueOf(bill.getElectricityCost()) + " pln"));
            orderedList.add(new ListItem("Zimna woda: " + String.valueOf(bill.getColdWaterCost()) + " pln"));
            orderedList.add(new ListItem("Ciepla woda: " + String.valueOf(bill.getHotWaterCost()) + " pln"));
            orderedList.add(new ListItem("Ogrzewanie: " + String.valueOf(bill.getHeatingCost()) + "  pln"));
            document.add(orderedList);
        } catch (DocumentException ex) {
            throw new ServerException("error in generating pdf file");
        } catch (Exception e) {
            throw new ServerException("error in generating pdf file");
        } finally {
            document.close();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }
}
