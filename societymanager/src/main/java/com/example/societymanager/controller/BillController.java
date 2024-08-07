package com.example.societymanager.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api")
@SessionAttributes("formData")
public class BillController {
    private static final Logger Log=LogManager.getLogger(BillController.class);
//    public FormController formController;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

//    @GetMapping("/bill")
//    public String showForm(Model model) {
//        if (!model.containsAttribute("formData")) {
//            // Initialize with empty values if formData does not exist in the session
//            Map<String, String> formData = new HashMap<>();
//            formData.put("room_no", "");
//            formData.put("bill_no", "");
//            formData.put("bill_date", "");
//            formData.put("maintenance_contribution", "");
//            formData.put("amount_44593", "");
//            formData.put("housing_board_contribution", "");
//            formData.put("sub_charge", "");
//            formData.put("property_tax_contribution", "");
//            formData.put("fine", "");
//            formData.put("sinking_fund", "");
//            formData.put("building_dev_fund", "");
//            formData.put("other", "");
//            formData.put("reserve_mhada_service_charge", "");
//            formData.put("current_month_total", "");
//            formData.put("arrears", "");
//            formData.put("amount_due", "");
//            model.addAttribute("formData", formData);
//        }
//        return "bill_form"; // Thymeleaf template for the form
//    }
//
//    @PostMapping("/bill/submit")
//    public String handleSubmit(@RequestParam Map<String, String> formData, Model model) {
////         Add form data to the model to be displayed in bill.html
//        model.addAttribute("formData", formData);
//        model.addAttribute("name", formData.get("name"));
//        model.addAttribute("roomNo", formData.get("room_no"));
//        model.addAttribute("billNo", formData.get("bill_no"));
//        model.addAttribute("billDate", formData.get("bill_date"));
//        model.addAttribute("maintenanceContribution", formData.get("maintenance_contribution"));
//        model.addAttribute("amount44593", formData.get("amount_44593"));
//        model.addAttribute("housingBoardContribution", formData.get("housing_board_contribution"));
//        model.addAttribute("subCharge", formData.get("sub_charge"));
//        model.addAttribute("propertyTaxContribution", formData.get("property_tax_contribution"));
//        model.addAttribute("fine", formData.get("fine"));
//        model.addAttribute("sinkingFund", formData.get("sinking_fund"));
//        model.addAttribute("buildingDevFund", formData.get("building_dev_fund"));
//        model.addAttribute("other", formData.get("other"));
//        model.addAttribute("reserveMhadaServiceCharge", formData.get("reserve_mhada_service_charge"));
//        model.addAttribute("currentMonthTotal", formData.get("current_month_total"));
//        model.addAttribute("arrears", formData.get("arrears"));
//        model.addAttribute("amountDue", formData.get("amount_due"));
//
//        return "bill"; // Thymeleaf template for the bill
////        formController.handleSubmit(formData, model);
////        return "bill";
//    }

    @PostMapping("/bill/modify")
    public String modifyBill(@ModelAttribute("formData") Map<String, String> formData, Model model) {
        model.addAllAttributes(formData);
        return "bill_form"; // Return to the form with pre-filled values
    }

    @GetMapping("/preview-pdf")
    @SuppressWarnings("unchecked")
    public String previewPdf(HttpServletRequest request, Model model) {
        Log.info("Hello");
        HttpSession session = request.getSession();
        try {
            // Retrieve formData from session attributes
            Map<String, String> formData = (Map<String, String>) session.getAttribute("formData");
            if (formData == null) {
                model.addAttribute("error", "No form data available");
                return "error"; // Thymeleaf template for the error page
            }

//            String html = generateBill(formData);
//            System.out.println("Generated html: "+html);
//            Log.info("Generated html: "+html);
            // Prepare Thymeleaf context
            Context context = new Context();
            context.setVariable("formData", formData);

            // Generate HTML content from Thymeleaf template
            String html = thymeleafViewResolver.getTemplateEngine().process("final_bill", context);
            Log.info("Generated HTML: " + html);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            HtmlConverter.convertToPdf(new ByteArrayInputStream(html.getBytes()), byteArrayOutputStream);
            byte[] pdfBytes = byteArrayOutputStream.toByteArray();

            //Encode PDF bytes to Base64
            String base64Pdf = Base64.getEncoder().encodeToString(pdfBytes);
            session.setAttribute("pdfBytes", base64Pdf);
            model.addAttribute("pdfBytes", base64Pdf);

            return "preview_bill"; // Thymeleaf template for previewing the bill
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Failed to generate PDF preview");
            return "error"; // Thymeleaf template for the error page
        }
    }

    @GetMapping("/download-pdf")
    public void downloadPdf(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        try {
            String base64Pdf = (String) session.getAttribute("pdfBytes");
            if (base64Pdf == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "No PDF available for download");
                return;
            }

            // Decode Base64 string to byte array
            byte[] pdfBytes = Base64.getDecoder().decode(base64Pdf);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=bills.pdf");
            response.setContentLength(pdfBytes.length);

            try (OutputStream outputStream = response.getOutputStream()) {
                outputStream.write(pdfBytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to download PDF");
        }
    }

//    private String generateBill(Map<String, String> formData) {
//        StringBuilder html = new StringBuilder();
//        html.append("<html><body>");
//        html.append("<h2 style='text-align: center;'>SHUKLENDU CO OP HOUSING SOCIETY LIMITED</h2>");
//
//        html.append("<div>");
//        html.append("<table border='1' style='width: 100%; border-collapse: collapse;'>");
//        html.append("<tr><th>ROOM NO:</th><td>").append(formData.get("room_no")).append("</td></tr>");
//        html.append("<tr><th>BILL NO:</th><td>").append(formData.get("bill_no")).append("</td></tr>");
//        html.append("<tr><th>BILL DATE:</th><td>").append(formData.get("bill_date")).append("</td></tr>");
//        html.append("</table>");
//
//        html.append("<table border='1' style='width: 100%; border-collapse: collapse;'>");
//        html.append("<tr><th>SR NO</th><th>DETAILS</th><th>AMOUNT</th><th>SR NO</th><th>AMOUNT</th></tr>");
//        html.append("<tr><td>1</td><td>Maintenance Contribution</td><td>").append(formData.get("maintenance_contribution")).append("</td><td>44593</td><td>").append(formData.get("amount_44593")).append("</td></tr>");
//        html.append("<tr><td>2</td><td>Housing Board Contribution</td><td>").append(formData.get("housing_board_contribution")).append("</td><td>Sub Charge</td><td>").append(formData.get("sub_charge")).append("</td></tr>");
//        html.append("<tr><td>3</td><td>Property Tax Contribution</td><td>").append(formData.get("property_tax_contribution")).append("</td><td>Fine</td><td>").append(formData.get("fine")).append("</td></tr>");
//        html.append("<tr><td>4</td><td>Sinking Fund</td><td>").append(formData.get("sinking_fund")).append("</td><td>Building Dev Fund</td><td>").append(formData.get("building_dev_fund")).append("</td></tr>");
//        html.append("<tr><td></td><td></td><td></td><td>Other</td><td>").append(formData.get("other")).append("</td></tr>");
//        html.append("<tr><td></td><td></td><td></td><td>Reserve Mhada Service Charge</td><td>").append(formData.get("reserve_mhada_service_charge")).append("</td></tr>");
//        html.append("</table>");
//
//        html.append("<table border='1' style='width: 100%; border-collapse: collapse;'>");
//        html.append("<tr><td></td><td>Current Month Total</td><td>Rs</td><td>").append(formData.get("current_month_total")).append("</td></tr>");
//        html.append("<tr><td></td><td>Arrears</td><td>Rs.</td><td>").append(formData.get("arrears")).append("</td></tr>");
//        html.append("<tr><td></td><td>Amount Due</td><td>Rs.</td><td>").append(formData.get("amount_due")).append("</td></tr>");
//        html.append("</table>");
//
//        html.append("</div>");
//        html.append("</body></html>");
//        return html.toString();
//    }
}
