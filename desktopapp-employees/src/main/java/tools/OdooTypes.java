package tools;

import java.io.ByteArrayInputStream;
import java.io.File;
import dao.GenericDAO;
import javafx.scene.image.Image;
import javax.xml.bind.DatatypeConverter;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OdooTypes
{
    public static final Image NO_IMAGE = new Image(new File("src/main/resources/com/dam/group2/desktopapp/" +
            "employees/images/found.png").toURI().toString());

    public static int getInt(HashMap map, String field)
    {
        int value = 0;

        Object object = map.get(field);
        if(object instanceof Integer)
            value = (int) object;

        return value;
    }

    public static double getDouble(HashMap map, String field)
    {
        double value = 0.0;

        Object object = map.get(field);
        if (object instanceof Double)
            value = (Double) object;

        return value;
    }

    public static String getString(HashMap map, String field)
    {
        String value = null;

        Object object = map.get(field);
        if(object instanceof String)
            value = (String) object;

        return value;
    }

    public static Date getDate(HashMap map, String field) throws ParseException {
        Date value = null;

        Object object = map.get(field);
        if(object instanceof String)
            value = Date.valueOf((String) object);

        return value;
    }

    public static Image getImage(HashMap map, String field)
    {
        Image value = NO_IMAGE;

        Object object = map.get(field);
        if(object instanceof String)
        {
            String base64String = (String) object;
            String[] strings = base64String.split(",");
            value = new Image(new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(strings[0])));
        }

        return value;
    }

    public static Object Many2one(HashMap map, String field, String classNameDAO) throws Exception
    {
        int value = 0;

        Object[] objects = (Object[]) map.get(field);
        if (objects.length > 0)
            value = (Integer) objects[0];

        return ((GenericDAO) Class.forName(classNameDAO).getDeclaredConstructor().newInstance())
                .findByPk(value);
    }

    public static List<Integer> One2many(HashMap map, String field)
    {
        List<Integer> values = new ArrayList<>();

        Object[] objects = (Object[]) map.get(field);
        if (objects.length > 0) {
            for (Object object : objects)
                values.add((Integer) object);
        }
        return values;
    }
}