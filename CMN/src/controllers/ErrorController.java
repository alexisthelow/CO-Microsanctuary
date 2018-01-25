package controllers;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorController {

	@RequestMapping(path = "errors.do", method = RequestMethod.GET)
	public ModelAndView renderErrorPage(HttpServletRequest httpRequest) {
		ModelAndView mv = new ModelAndView("error");
		String errorMsg = "";
		int httpErrorCode = getErrorCode(httpRequest);

		switch (httpErrorCode) {
			case 400: {
				errorMsg = "Http Error Code: 400. Bad Request";
				// TODO - page?
				break;
			}
			case 404: {
				errorMsg = "Http Error Code: 404. Resource not found";
				// TODO - page?
				break;
			}
			case 500: {
				errorMsg = "Http Error Code: 500. Internal Server Error";
				// TODO - page?
				break;
			}
		}
		mv.addObject("errorMsg", errorMsg);
		return mv;
	}

	private int getErrorCode(HttpServletRequest httpRequest) {
		return (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");
	}

}
