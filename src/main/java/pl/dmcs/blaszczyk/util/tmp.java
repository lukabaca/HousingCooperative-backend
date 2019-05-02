package pl.dmcs.blaszczyk.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import pl.dmcs.blaszczyk.model.Entity.Bill;
import pl.dmcs.blaszczyk.model.Exception.ServerException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class tmp {
    public static ByteArrayInputStream billReport(Bill bill) {
        if (bill == null) {
            return null;
        }
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(60);
            table.setWidths(new int[]{1, 4, 4});
            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

            PdfPCell hcell;
            hcell = new PdfPCell(new Phrase("Prąd", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Ciepła woda", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Zimna woda", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Ogrzewanie", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            PdfPCell cell;
            cell = new PdfPCell(new Phrase(String.valueOf((bill.getElectricityCost()))));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(bill.getHotWaterCost())));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(bill.getColdWaterCost())));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(bill.getHeatingCost())));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);

            PdfWriter.getInstance(document, out);
            document.open();
//            document.add(new Paragraph("RACHUNEK # " + String.valueOf(bill.getId())));
//            document.add(new Paragraph("Dla okresu: " + String.valueOf(bill.getMeasurement().getMonth()) + String.valueOf(bill.getMeasurement().getYear())));
//            document.add(new Paragraph("Rachunek wystawiony dla lokalu: " + String.valueOf(bill.getMeasurement().getPremise().getNumber())));
            document.add(table);
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
