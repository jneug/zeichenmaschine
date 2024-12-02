package schule.ngb.zm.util.test;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import schule.ngb.zm.Testmaschine;
import schule.ngb.zm.Zeichenmaschine;

public class TestEnv implements ParameterResolver {

	@Override
	public boolean supportsParameter( ParameterContext parameterContext, ExtensionContext extensionContext ) throws ParameterResolutionException {
		return (
			parameterContext.getParameter().getType() == Zeichenmaschine.class ||
				parameterContext.getParameter().getType() == Testmaschine.class
		);
	}

	@Override
	public Object resolveParameter( ParameterContext parameterContext, ExtensionContext extensionContext ) throws ParameterResolutionException {
		return new Testmaschine();
	}

}
