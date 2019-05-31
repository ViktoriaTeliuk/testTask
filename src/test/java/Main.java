import org.springframework.context.support.GenericXmlApplicationContext;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        try (GenericXmlApplicationContext appCtx = new GenericXmlApplicationContext()) {
            appCtx.load("spring/spring-app.xml", "spring/spring-db.xml");
            appCtx.refresh();

            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
        }

/*
        try{
            Class.forName("org.h2.Driver");
        }catch(ClassNotFoundException ex){
            System.out.println( "ERROR: " + ex.toString() );

        }
*/
    }
}
