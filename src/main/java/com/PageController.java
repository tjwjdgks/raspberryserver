package com;

import com.domain.Humidity;
import com.domain.Light;
import com.domain.Temperature;
import com.domain.UserAccount;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.repository.JpaHumidityRepository;
import com.repository.JpaLightRepository;
import com.repository.JpaTemperatureRepository;
import com.repository.JpaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class PageController {
    @Autowired
    JpaUserRepository jpaUserRepository;
    @Autowired
    JpaHumidityRepository jpaHumidityRepository;
    @Autowired
    JpaLightRepository jpaLightRepository;
    @Autowired
    JpaTemperatureRepository jpaTemperatureRepository;

    @RequestMapping(value="/hello")
    public String hello(Model model){
        return "hello";
    }
    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }
    @GetMapping("/test")
    public String test(){
        return "login";
    }
    @GetMapping("/")
    public String firstPage(){
        return "login";
    }
    @PostMapping("/login.do")
    @Transactional
    public String loginDo(HttpServletRequest httpServletRequest) throws Exception{
        System.out.println(httpServletRequest.getParameter("Email"));
        System.out.println(httpServletRequest.getParameter("FirstName"));
        System.out.println(httpServletRequest.getParameter("LastName"));
        System.out.println(httpServletRequest.getParameter("Password"));
        String name = httpServletRequest.getParameter("FirstName") + httpServletRequest.getParameter("LastName");
        UserAccount userAccount = new UserAccount();
        userAccount.setEmail(httpServletRequest.getParameter("Email"));
        userAccount.setPassword(httpServletRequest.getParameter("Password"));
        userAccount.setName(name);
        jpaUserRepository.save(userAccount);
        return "login";
    }
    @PostMapping("/login.check")
    public String loginCheck(HttpServletRequest httpServletRequest, Model model) throws Exception{
        System.out.println(httpServletRequest.getParameter("Email"));
        String email = httpServletRequest.getParameter("Email");
        String password = httpServletRequest.getParameter("Password");
        System.out.println(password);
        List<UserAccount> userAccounts = jpaUserRepository.findByEmailAndPassword(email,password);
        if(userAccounts.isEmpty()){
            return "login";
        }
        else{
            model.addAttribute("name",userAccounts.get(0).getName());
            float[][] lights = getLightData(email);
            float[] light = lights[0];
            int [] lightcount = new int[3];
            for(int i=0; i<3;i++){
                lightcount[i] = (int)lights[1][i];
                System.out.println(lightcount[i]);
            }
            float[][] humiditys = getHumidityData(email);
            float[] humidity = humiditys[0];
            int [] humiditycount = new int[3];
            for(int i=0; i<3; i++){
                humiditycount[i] = (int)humiditys[1][i];
            }
            float[][] temperatures = getTemperatureData(email);
            float[] temperature = temperatures[0];
            int[] temperaturecount = new int[3];
            for(int i=0; i<3;i++){
                temperaturecount[i] = (int)temperatures[1][i];
            }
            float totalAverageLight = 0;
            int totalCountLight = 0;
            for(float i : light){
                if(i != 0) {
                    totalAverageLight += i;
                    totalCountLight++;
                }
            }
            float totalAveragehumidity = 0;
            int totalCounthumidity = 0;
            for(float i : humidity){
                if(i != 0) {
                    totalAveragehumidity += i;
                    totalCounthumidity++;
                }
            }
            float totalAverageTemerature = 0;
            int totalCountTemerature = 0;
            for(float i : temperature){
                if(i != 0) {
                    totalAverageTemerature += i;
                    totalCountTemerature++;
                }
            }
            if(totalCountLight != 0)
                totalAverageLight /= totalCountLight;
            if(totalCounthumidity != 0)
                totalAveragehumidity /= totalCounthumidity;
            if(totalCountTemerature != 0)
                totalAverageTemerature /= totalCountTemerature;
            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String curdate = df.format(date);
            model.addAttribute("date",curdate);
            model.addAttribute("lightcount",lightcount);
            model.addAttribute("yearlight",totalAverageLight);
            model.addAttribute("monthlight",light);

            model.addAttribute("humiditycount",humiditycount);
            model.addAttribute("yearhumidity",totalAveragehumidity);
            model.addAttribute("monthhumidity",humidity);

            model.addAttribute("temperaturecount",temperaturecount);
            model.addAttribute("yeartemperature",totalAverageTemerature);
            model.addAttribute("monthtemperature",temperature);
            return "index";
        }
    }
    @GetMapping("/save.light")
    @ResponseBody
    public String saveLight(HttpServletRequest httpServletRequest) throws Exception{
        String email = httpServletRequest.getParameter("Email");
        float value = Float.parseFloat(httpServletRequest.getParameter("Value"));
        Light light = new Light();
        Date date = new Date();
        light.setEmail(email);
        light.setValue(value);
        light.setDate(date);
        jpaLightRepository.save(light);
        return "success";
    }
    @GetMapping("/save.humidity")
    @ResponseBody
    public String saveHumidity(HttpServletRequest httpServletRequest) throws Exception{
        String email = httpServletRequest.getParameter("Email");
        float value = Float.parseFloat(httpServletRequest.getParameter("Value"));
        Humidity humidity = new Humidity();
        Date date = new Date();
        humidity.setEmail(email);
        humidity.setValue(value);
        humidity.setDate(date);
        jpaHumidityRepository.save(humidity);
        return "success";
    }
    @GetMapping("/save.temperature")
    @ResponseBody
    public String saveTemperature(HttpServletRequest httpServletRequest) throws Exception{
        String email = httpServletRequest.getParameter("Email");
        float value = Float.parseFloat(httpServletRequest.getParameter("Value"));
        Temperature temperature = new Temperature();
        Date date = new Date();
        temperature.setEmail(email);
        temperature.setValue(value);
        temperature.setDate(date);
        jpaTemperatureRepository.save(temperature);
        return "success";
    }
    public float[][] getLightData(String Email){
        float [][]months = new float[2][12];
        int []count = new int[12];
        List<Light> arr = jpaLightRepository.findAllByEmail(Email);
        for(Light i : arr){
            Calendar cal = Calendar.getInstance();
            cal.setTime(i.getDate());
            int curmonth = cal.get(Calendar.MONTH);
            float curval = i.getValue();
            months[0][curmonth] += curval;
            count[curmonth] +=1;
            if(curval>2000)
                months[1][0]++;
            else if(curval<=2000 && curval>1000)
                months[1][1]++;
            else
                months[1][2]++;
        }
        for(int i=0; i<months[0].length;i++){
            if(count[i] == 0) continue;
            months[0][i] = months[0][i]/count[i];
        }
        return months;
    }
    public float[][] getHumidityData(String Email){
        float [][]months = new float[2][12];
        int []count = new int[12];
        List<Humidity> arr = jpaHumidityRepository.findAllByEmail(Email);
        for(Humidity i : arr){
            Calendar cal = Calendar.getInstance();
            cal.setTime(i.getDate());
            int curmonth = cal.get(Calendar.MONTH);
            float curval = i.getValue();
            months[0][curmonth] += curval;
            count[curmonth] +=1;
            if(curval > 70)
                months[1][0]++;
            else if(curval<=70 && curval > 60)
                months[1][1]++;
            else
                months[1][2]++;
        }
        for(int i=0; i<months[0].length;i++){
            if(count[i] == 0) continue;
            months[0][i] = months[0][i]/count[i];
        }
        return months;
    }
    public float[][] getTemperatureData(String Email){
        float [][]months = new float[2][12];
        int []count = new int[12];
        List<Temperature> arr = jpaTemperatureRepository.findAllByEmail(Email);
        for(Temperature i : arr){
            Calendar cal = Calendar.getInstance();
            cal.setTime(i.getDate());
            int curmonth = cal.get(Calendar.MONTH);
            float curval = i.getValue();
            months[0][curmonth] += curval;
            count[curmonth] +=1;
            if(curval > 25)
                months[1][0]++;
            else if(curval<=25 && curval>20)
                months[1][1]++;
            else
                months[1][2]++;
        }
        for(int i=0; i<months[0].length;i++){
            if(count[i] == 0) continue;
            months[0][i] = months[0][i]/count[i];
        }
        return months;
    }
}