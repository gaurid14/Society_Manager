package com.example.societymanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.support.SessionStatus;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api")
@SessionAttributes("formData")
public class FormController {
    private static final Logger Log=LogManager.getLogger(FormController.class);

    @GetMapping("/form")
    public String showForm(Model model) {
        if(!model.containsAttribute("formData")){
            // Initialize with empty values if formData does not exist in the session
            Map<String, String> formData = new HashMap<>();
//            formData.put("room_no", "");
//            formData.put("bill_no", "");
//            formData.put("bill_date", "");
            formData.put("maintenance_contribution", "");
            formData.put("sub_charge", "");
            formData.put("housing_board_contribution", "");
            formData.put("fine", "");
            formData.put("property_tax_contribution", "");
            formData.put("building_dev_fund", "");
            formData.put("sinking_fund", "");
            formData.put("other", "");
            formData.put("reserve_mhada_service_charge", "");
            formData.put("bill_for", "");
            formData.put("current_month_total", "");
            formData.put("amount_due_in_words", "");
            formData.put("arrears", "");
            formData.put("bldg_fund_due", "");
            formData.put("amount_due", "");
            model.addAttribute("formData", formData);
        }
        return "bill_form"; // Thymeleaf template for the form
    }

    @PostMapping("/form/submit")
    public String handleSubmit(@RequestParam Map<String, String> formData, Model model) {
        // Add form data to the session attributes
        model.addAttribute("formData", formData);

        // Add form data to the model to be displayed in bill.html
//        model.addAttribute("roomNo", formData.get("room_no"));
//        model.addAttribute("billNo", formData.get("bill_no"));
//        model.addAttribute("billDate", formData.get("bill_date"));
        model.addAttribute("maintenanceContribution", formData.get("maintenance_contribution"));
        model.addAttribute("sub_charge", formData.get("sub_charge"));
        model.addAttribute("housing_board_contribution", formData.get("housing_board_contribution"));
        model.addAttribute("fine", formData.get("fine"));
        model.addAttribute("property_tax_contribution", formData.get("property_tax_contribution"));
        model.addAttribute("building_dev_fund", formData.get("building_dev_fund"));
        model.addAttribute("sinking_fund", formData.get("sinking_fund"));
        model.addAttribute("other", formData.get("other"));
        model.addAttribute("reserve_mhada_service_charge", formData.get("reserve_mhada_service_charge"));
        model.addAttribute("bill_for", formData.get("bill_for"));
        model.addAttribute("current_month_total", formData.get("current_month_total"));
        model.addAttribute("amount_due_in_words", formData.get("amount_due_in_words"));
        model.addAttribute("arrears", formData.get("arrears"));
        model.addAttribute("bldg_fund_due", formData.get("bldg_fund_due"));
        model.addAttribute("amount_due", formData.get("amount_due"));

        return "bill"; // Thymeleaf template for the bill
    }

    @PostMapping("/form/modify")
    public String modifyBill(@ModelAttribute("formData") Map<String, String> formData, Model model) {
        // Add form data back to the model to pre-fill the form fields
        model.addAllAttributes(formData);
        return "bill_form"; // Return to the form with pre-filled values
    }
}
