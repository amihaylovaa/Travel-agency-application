package adelina.luxtravel;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.Assert.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class SomeClassIT {

    @Test
    public void example(){
        assertTrue(true);
    }
}
