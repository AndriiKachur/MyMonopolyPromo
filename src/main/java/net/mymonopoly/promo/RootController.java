package net.mymonopoly.promo;

import java.util.Date;
import java.util.Enumeration;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RootController {
	
	private static final Log LOG = LogFactory.getLog(RootController.class);

	@RequestMapping(method = RequestMethod.GET, value = "/")
	public String root() {
		return "promo";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/")
	public String save(@RequestParam String email, HttpServletRequest request) {
		try {
			if (Email.findEmailsByEmailEquals(email).getResultList().size() > 0 ) {
				return "promo_thanks";
			}		
		
			Email emaile = new Email();
			emaile.setEmail(email);
			emaile.setSubscribeDate(new Date());
			emaile.persist();
			emaile = Email.findEmailsByEmailEquals(email).getSingleResult();

			Enumeration<String> headerNames = (Enumeration<String>) request
					.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String name = headerNames.nextElement();
				BrowserHeaders bh = new BrowserHeaders();
				bh.setName(name);
				bh.setVal(request.getHeader(name));
				bh.setEmail(emaile);
				bh.persist();
			}
		} catch (Throwable e) {
			LOG.error(email, e);
			return "promo";
		}
		return "promo_thanks";
	}

}
