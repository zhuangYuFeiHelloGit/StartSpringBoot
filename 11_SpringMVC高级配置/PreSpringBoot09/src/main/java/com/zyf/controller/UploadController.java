package com.zyf.controller;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/**
 * Created by zyf on 2018/3/5.
 */
@Controller
public class UploadController {

	@RequestMapping(value = "/upload",method = RequestMethod.POST)
	@ResponseBody
	public String upload(MultipartFile file, HttpServletRequest request){
		try {
			FileUtils.writeByteArrayToFile(new File("/Users/zyf/Desktop/1_资料/JavaEE/阶段-高级/SpringBoot/11_SpringMVC高级配置/upload/"+file.getOriginalFilename()),file.getBytes());
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "error";
	}
}
