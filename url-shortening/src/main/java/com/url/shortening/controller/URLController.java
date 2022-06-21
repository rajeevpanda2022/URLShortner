package com.url.shortening.controller;

import com.url.shortening.ApplicationProperties;
import com.url.shortening.entity.URLData;
import com.url.shortening.model.URLModel;
import com.url.shortening.repository.URLRepository;
import com.url.shortening.util.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;

@Controller
public class URLController {

    private URLRepository urlRepository;
    private ApplicationProperties applicationProperties;

    @Autowired
    public void setApp(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Autowired
    public void setURLRepository(URLRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("urlModel", new URLModel());
        //urlRepository.deleteAll();
        return "home";
    }

    @RequestMapping("/short")
    public String shortUrl(Model model, @ModelAttribute URLModel urlModel) {
        String hash = HashUtil.hash(urlModel.getUrl());
        String baseURI = applicationProperties.baseURI;

        urlModel.setHash(hash);
        urlModel.setBaseURI(baseURI);

        URLData domain = urlRepository.findFirstByHash(hash);
        if (domain != null && !domain.getUrl().isEmpty()) {
            System.out.println(domain.getHash());
            System.out.println(domain.getUrl());
        } else {
            URLData d = new URLData();
            d.setHash(hash);
            d.setUrl(urlModel.getUrl());
            urlRepository.save(d);
        }

        System.out.println(urlRepository.count());

        model.addAttribute("urlModel", urlModel);

        return "result";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public void redirect(@PathVariable String id, HttpServletResponse resp) throws Exception {

        URLData domain = urlRepository.findFirstByHash(id);

        if (domain != null && domain.getUrl() != null) {
            resp.sendRedirect(domain.getUrl());
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

}
