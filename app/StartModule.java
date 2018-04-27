import com.google.inject.AbstractModule;

public class StartModule extends AbstractModule {
  protected void configure() {
    bind(ApplicationStart.class).asEagerSingleton();
  }
}
