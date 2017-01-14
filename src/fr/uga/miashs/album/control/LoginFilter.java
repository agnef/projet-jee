package fr.uga.miashs.album.control;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import fr.uga.miashs.album.util.Pages;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter("/faces/*")
public class LoginFilter implements Filter {

	@Inject
	private AppUserSession appUserSession;

	public String[] filteredPagesNoLogin;
	public String[] filteredPagesUser;

	public String[] filteredPagesAdmin;

	/**
	 * Default constructor.
	 */
	public LoginFilter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// toutes les pages qu'on veut filtrer, on peut en ajouter
		filteredPagesNoLogin = new String[] { Pages.add_album, Pages.list_album, Pages.list_user };
		filteredPagesAdmin = new String[] { Pages.add_album, Pages.list_album };
		filteredPagesUser = new String[] { Pages.list_user };

	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String requestedUri = ((HttpServletRequest) request).getRequestURI()
				.substring(((HttpServletRequest) request).getContextPath().length() + 1);

		for (String s : filteredPagesAdmin) {
			if (s.equals(requestedUri)) {
				if (appUserSession != null && appUserSession.getConnectedUser() != null
						&& appUserSession.getConnectedUser().getEmail().equals("admin@admin.fr")) {
					request.getRequestDispatcher(Pages.list_user).forward(request, response);
					System.out.println(appUserSession.getConnectedUser().getEmail());
				}
			}
		}

		for (String s : filteredPagesUser) {
			if (s.equals(requestedUri)) {
				if (appUserSession != null && appUserSession.getConnectedUser() != null
						&& !appUserSession.getConnectedUser().getEmail().equals("admin@admin.fr")) {
					request.getRequestDispatcher(Pages.list_album).forward(request, response);
					System.out.println(appUserSession.getConnectedUser().getEmail());

				}
			}
		}

		for (String s : filteredPagesNoLogin) {
			if (s.equals(requestedUri)) {
				if (appUserSession == null || appUserSession.getConnectedUser() == null) {
					// si il n'y a pas de session ouverte alors toutes les
					// pages mènent à la page d'accueil
					request.getRequestDispatcher(Pages.accueil).forward(request, response);
					System.out.println("personne de connecté");
				}
			}
		}

		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

}
