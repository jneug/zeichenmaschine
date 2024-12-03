package schule.ngb.zm.particles;

import schule.ngb.zm.util.Log;

import java.util.Arrays;
import java.util.function.Supplier;

public class GenericParticleFactory<T extends Particle> implements ParticleFactory {

	private final Class<T> type;

	private final Supplier<T> supplier;

	public GenericParticleFactory( Class<T> type, Object... params ) {
		this.type = type;

		// Create paramTypes array once
		Class<?>[] paramTypes = new Class<?>[params.length];
		for( int i = 0; i < params.length; i++ ) {
			paramTypes[i] = params[i].getClass();
		}

		this.supplier = () -> {
			T p = null;
			try {
				p = GenericParticleFactory.this.type.getDeclaredConstructor(paramTypes).newInstance(params);
			} catch( Exception ex ) {
				LOG.error( ex,
					"Unable to create new Particle of type %s",
					GenericParticleFactory.this.type.getCanonicalName()
				);
			}
			return p;
		};
	}

	public GenericParticleFactory( Supplier<T> supplier ) {
		this.supplier = supplier;
		this.type = (Class<T>)supplier.get().getClass();
	}

	@Override
	public Particle createParticle() {
		return this.supplier.get();
	}

	private static final Log LOG = Log.getLogger(GenericParticleFactory.class);

}
