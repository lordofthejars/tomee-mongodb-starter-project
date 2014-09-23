package org.superbiz.persistence;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.superbiz.Color;
import org.superbiz.ColorService;

@RunWith(Arquillian.class)
public class ColorTest {

	private static final String MONGODB_RESOURCE = "<resources>\n" + 
			"    <Resource id=\"mongoUri\" class-name=\"com.mongodb.MongoClientURI\" constructor=\"uri\">\n" + 
			"        uri  mongodb://localhost/test\n" + 
			"    </Resource>\n" + 
			"</resources>";
	
    @Deployment
    public static JavaArchive createDeployment() {
        JavaArchive javaArchive = ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, Color.class.getPackage())
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource(new StringAsset(MONGODB_RESOURCE), "resources.xml")
                .merge(getJongoAndMongoDependecies());
        
        return javaArchive;
    }

    private static JavaArchive getJongoAndMongoDependecies() {
        JavaArchive[] javaArchives = Maven.configureResolver()
                .loadPomFromFile("pom.xml")
                .resolve("org.mongodb:mongo-java-driver", "org.jongo:jongo")
                .withTransitivity()
                .as(JavaArchive.class);

        JavaArchive mergedLibraries = ShrinkWrap.create(JavaArchive.class);

        for (JavaArchive javaArchive : javaArchives) {
            mergedLibraries.merge(javaArchive);
        }

        return mergedLibraries;
    }

    @EJB
    ColorService colorService;

    @Before
    public void cleanDatabase() {
       colorService.removeAllColors();
    }

    @Test
    public void should_insert_color() {

        Color color = colorService.createColor(new Color("red", 255, 0, 0));

        assertThat(color.getId(), notNullValue());
        assertThat(color.getName(), is("red"));
        assertThat(color.getR(), is(255));
        assertThat(color.getB(), is(0));
        assertThat(color.getG(), is(0));

    }


    @Test
    public void should_count_number_of_colors() {

        colorService.createColor(new Color("red", 255, 0, 0));
        colorService.createColor(new Color("blue", 0, 0, 255));

        assertThat(colorService.countColors(), is(2L));

    }

    @Test
    public void should_find_colors_by_id() {

        Color originalColor = colorService.createColor(new Color("red", 255, 0, 0));

        Color color = colorService.findColorById(originalColor.getId());

        assertThat(color.getId(), notNullValue());
        assertThat(color.getName(), is("red"));
        assertThat(color.getR(), is(255));
        assertThat(color.getB(), is(0));
        assertThat(color.getG(), is(0));

    }

    @Test
    public void should_find_colors_by_name() {

        colorService.createColor(new Color("red", 255, 0, 0));

        Color color = colorService.findColorByColorName("red");

        assertThat(color.getId(), notNullValue());
        assertThat(color.getName(), is("red"));
        assertThat(color.getR(), is(255));
        assertThat(color.getB(), is(0));
        assertThat(color.getG(), is(0));


    }

    @Test
    public void should_find_colors_by_red() {

        colorService.createColor(new Color("red", 255, 0, 0));
        colorService.createColor(new Color("white", 255, 255, 255));

        Iterable<Color> colorByRed = colorService.findColorByRed(255);

        assertThat(colorByRed, hasItems(new Color("red", 255, 0, 0), new Color("white", 255, 255, 255)));

    }

}