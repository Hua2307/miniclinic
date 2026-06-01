package tw.edu.fju.miniclinic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;
import tw.edu.fju.miniclinic.model.Doctor;
import tw.edu.fju.miniclinic.model.DoctorRepository;
import tw.edu.fju.miniclinic.model.PasswordForm;

@Controller
public class PasswordController {

    @Autowired
    private DoctorRepository doctorRepo;

    @GetMapping("/password")
    public String showPasswordForm(HttpSession session, Model model) {
        String loggedInDoctorName = (String) session.getAttribute("loggedInDoctorName");
        
        model.addAttribute("loggedInDoctorName", loggedInDoctorName);
        model.addAttribute("form", new PasswordForm());
        return "password";
    }

    @PostMapping("/password")
    public String updatePassword(
            @ModelAttribute("form") PasswordForm form,
            HttpSession session,
            Model model) {

        String loggedInDoctorId = (String) session.getAttribute("loggedInDoctorId");
        String loggedInDoctorName = (String) session.getAttribute("loggedInDoctorName");
        
        // 查詢醫師
        Doctor doctor = doctorRepo.findById(loggedInDoctorId).orElse(null);
        if (doctor == null) {
            model.addAttribute("error", "找不到醫師資訊");
            model.addAttribute("form", form);
            model.addAttribute("loggedInDoctorName", loggedInDoctorName);
            return "password";
        }

        // 驗證舊密碼
        if (!BCrypt.checkpw(form.getOldPassword(), doctor.getPasswordHash())) {
            model.addAttribute("error", "舊密碼錯誤");
            model.addAttribute("form", form);
            model.addAttribute("loggedInDoctorName", loggedInDoctorName);
            return "password";
        }

        // 驗證新密碼與確認密碼是否一致
        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
            model.addAttribute("error", "兩次密碼不相符");
            model.addAttribute("form", form);
            model.addAttribute("loggedInDoctorName", loggedInDoctorName);
            return "password";
        }

        // 驗證新密碼長度
        if (form.getNewPassword().length() < 8) {
            model.addAttribute("error", "密碼至少需要 8 個字元");
            model.addAttribute("form", form);
            model.addAttribute("loggedInDoctorName", loggedInDoctorName);
            return "password";
        }

        // 驗證通過，更新密碼
        String newHash = BCrypt.hashpw(form.getNewPassword(), BCrypt.gensalt());
        System.out.println("DEBUG: 醫師 " + loggedInDoctorId + " 正在修改密碼。新雜湊值為: " + newHash);
        System.out.println("DEBUG: 原密碼雜湊值為: " + doctor.getPasswordHash());
        System.out.println("DEBUG: 新密碼雜湊值為: " + newHash);
        
        doctor.setPasswordHash(newHash);
        doctorRepo.save(doctor);

        model.addAttribute("success", "密碼修改成功！");
        model.addAttribute("form", new PasswordForm());
        model.addAttribute("loggedInDoctorName", loggedInDoctorName);
        return "password";
    }
}
