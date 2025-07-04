package indradwi_restfull.resolver;

import indradwi_restfull.entity.User;
import indradwi_restfull.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

	@Autowired
	private UserRepository userRepository;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return User.class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
	                              NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

		HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
		String token = request.getHeader("X-API-TOKEN"); // ✅ perbaiki di sini
		log.info("Token: {}", token);

		if (token == null || token.isBlank()) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
		}

		User user = userRepository.findFirstByToken(token)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));

		if (user.getTokenExpireAt() == null || user.getTokenExpireAt() < System.currentTimeMillis()) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token expired");
		}

		return user;
	}
}
