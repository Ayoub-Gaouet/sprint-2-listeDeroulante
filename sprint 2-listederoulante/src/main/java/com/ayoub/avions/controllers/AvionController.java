package com.ayoub.avions.controllers;

import com.ayoub.avions.entities.Avion;
import com.ayoub.avions.entities.Company;
import com.ayoub.avions.service.AvionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class AvionController{
    @Autowired
    AvionService avionService;

    @RequestMapping("/showCreate")
    public String showCreate(ModelMap modelMap)
    {
        List<Company> coms = avionService.getAllCompanys();
        Avion prod = new Avion();
        Company com = new Company();
        com = coms.get(0); // prendre la première catégorie de la liste
        prod.setCompany(com); //affedter une catégorie par défaut au produit pour éviter le pb avec une catégorie null
        modelMap.addAttribute("avion",prod);
        modelMap.addAttribute("mode", "new");
        modelMap.addAttribute("companys", coms);
        return "formAvion";
    }

    @RequestMapping("/saveAvion")
    public String saveAvion(@Valid Avion avion, BindingResult bidingResult)
    {
        if (bidingResult.hasErrors())return "formAvion";
        avionService.saveAvion(avion);
        return "formAvion";
    }

    @RequestMapping("/ListeAvions")
    public String listeAvions(ModelMap modelMap, @RequestParam(name = "page", defaultValue = "0") int page,
                              @RequestParam(name = "size", defaultValue = "2") int size) {
        Page<Avion> sers = avionService.getAllAvionsParPage(page, size);
        modelMap.addAttribute("avions", sers);
        modelMap.addAttribute("pages", new int[sers.getTotalPages()]);
        modelMap.addAttribute("currentPage", page);
        return "listeAvions";
    }

    @RequestMapping("/supprimerAvion")
    public String supprimerAvion(@RequestParam("id") Long id, ModelMap modelMap,
                                 @RequestParam(name = "page", defaultValue = "0") int page,
                                 @RequestParam(name = "size", defaultValue = "2") int size) {
        avionService.deleteAvionById(id);
        Page<Avion> sers = avionService.getAllAvionsParPage(page, size);
        modelMap.addAttribute("avions", sers);
        modelMap.addAttribute("pages", new int[sers.getTotalPages()]);
        modelMap.addAttribute("currentPage", page);
        modelMap.addAttribute("size", size);
        return "listeAvions";
    }

    @RequestMapping("/modifierAvion")
    public String editerProduit(@RequestParam("id") Long id,ModelMap modelMap)
    {
        Avion a=avionService.getAvion(id);
        List<Company> coms = avionService.getAllCompanys();
        modelMap.addAttribute("avion", a);
        modelMap.addAttribute("mode", "edit");
        modelMap.addAttribute("companys", coms);
        return "formAvion";
    }

    @RequestMapping("/updateAvion")
    public String updateAvion(@ModelAttribute("avion") Avion avion,
                              @RequestParam("date") String date,
                              ModelMap modelMap) throws ParseException
    {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateCreation = dateformat.parse(String.valueOf(date));
        avion.setDateCreation(dateCreation);

        avionService.updateAvion(avion);
        List<Avion> prods = avionService.getAllAvions();
        modelMap.addAttribute("avions", prods);
        return "listeAvions";
    }
}
