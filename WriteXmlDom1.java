import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.Scanner;

public class WriteXmlDom1 {



    public static void main(String[] args)
            throws ParserConfigurationException, TransformerException {
              Scanner ob = new Scanner(System.in);
              System.out.println("Enter latitude coordinate:");
              String x = ob.nextLine();
              System.out.println("Enter longitude coordinate:");
              String y = ob.nextLine();
              //String x = "37.639399"; //replace with input
              //String y = "-120.946480"; //replace with input

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // root elements
        Document doc = docBuilder.newDocument();

        //Element zero = doc.createElement("");
        //CDATASection zeron = doc.createCDATASection("!DOCTYPE flight_plan SYSTEM \"flight_plan.dtd\"");
        //zero.setAttribute(zeron);
        //doc.appendChild(zero);

        Element one = doc.createElement("flight_plan");
        one.setAttribute("alt", "75");
        one.setAttribute("ground_alt", "0");
        one.setAttribute("lat0", "37.6390948");
        one.setAttribute("lon0", "-120.9448663");
        one.setAttribute("max_dist_from_home", "13000");
        one.setAttribute("name", "FarmToTable");
        one.setAttribute("security_height", "25");
        doc.appendChild(one);

        Element two = doc.createElement("header");
        //Comment inc = doc.createComment("#include \"subsystems/datalink/datalink.h\"");
        //two.appendChild(inc);
        one.appendChild(two);

        Element three = doc.createElement("waypoints");
        Element uno = doc.createElement("waypoint");
        uno.setAttribute("name", "HOME");
        uno.setAttribute("x", "0");
        uno.setAttribute("y", "0");
        Element dos = doc.createElement("waypoint");
        dos.setAttribute("name", "STDBY");
        dos.setAttribute("x", "49.5");
        dos.setAttribute("y", "100.1");
        Element tres = doc.createElement("waypoint");
        tres.setAttribute("alt", "30.0");
        tres.setAttribute("name", "AF");
        tres.setAttribute("x", "100.0");
        tres.setAttribute("y", "-65.0");
        Element cuatro = doc.createElement("waypoint");
        cuatro.setAttribute("alt", "0.0");
        cuatro.setAttribute("name", "TD");
        cuatro.setAttribute("x", "-52.0");
        cuatro.setAttribute("y", "-50.0");
        Element cinco = doc.createElement("waypoint");
        cinco.setAttribute("name", "_BASELEG");
        cinco.setAttribute("x", "168.8");
        cinco.setAttribute("y", "-13.8");
        Element seis = doc.createElement("waypoint");
        seis.setAttribute("name", "CLIMB");
        seis.setAttribute("x", "-190.0");
        seis.setAttribute("y", "0.0");
        Element siete = doc.createElement("waypoint");
        siete.setAttribute("name", "DEL");
        siete.setAttribute("lat", x);
        siete.setAttribute("lon", y);
        three.appendChild(uno);
        three.appendChild(dos);
        three.appendChild(tres);
        three.appendChild(cuatro);
        three.appendChild(cinco);
        three.appendChild(seis);
        three.appendChild(siete);
        one.appendChild(three);

        Element four = doc.createElement("variables");
        Element first = doc.createElement("variable");
        first.setAttribute("var", "drop_payload");
        first.setAttribute("init", "0");
        first.setAttribute("type", "int");
        four.appendChild(first);
        one.appendChild(four);

        Element five = doc.createElement("blocks");
        Element julius = doc.createElement("block");
        julius.setAttribute("name", "Wait GPS");
        Element july = doc.createElement("set");
        july.setAttribute("value", "1");
        july.setAttribute("var", "autopilot.kill_throttle");
        Element julia = doc.createElement("while");
        julia.setAttribute("cond", "!GpsFixValid()");
        Element augustus = doc.createElement("block");
        augustus.setAttribute("name", "Geo init");
        Element august = doc.createElement("while");
        august.setAttribute("cond", "LessThan(NavBlockTime(), 10)");
        Element octavia = doc.createElement("call_once");
        octavia.setAttribute("fun", "NavSetGroundReferenceHere()");
        Element traianus = doc.createElement("block");
        traianus.setAttribute("name", "Holding point");
        Element traia = doc.createElement("set");
        traia.setAttribute("value", "1");
        traia.setAttribute("var", "autopilot.kill_throttle");
        Element ulpia = doc.createElement("attitude");
        ulpia.setAttribute("roll", "0");
        ulpia.setAttribute("throttle", "0");
        ulpia.setAttribute("vmode", "throttle");
        Element aurelianus = doc.createElement("block");
        aurelianus.setAttribute("key", "t");
        aurelianus.setAttribute("name", "Takeoff");
        aurelianus.setAttribute("strip_button", "Takeoff (wp CLIMB)");
        aurelianus.setAttribute("strip_icon", "takeoff.png");
        aurelianus.setAttribute("group", "home");
        //CDATASection aurel = doc.createCDATASection("<exception cond=\"GetPosAlt() > GetAltRef()+25\" deroute=\"Standby\"/>");
        Element aurel = doc.createElement("exception");
        aurel.setAttribute("cond", "GetPosAlt() > GetAltRef()+25");
        aurel.setAttribute("deroute", "Standby");
        Element aurelia = doc.createElement("set");
        aurelia.setAttribute("value", "0");
        aurelia.setAttribute("var", "autopilot.kill_throttle");
        Element occidenticus = doc.createElement("set");
        occidenticus.setAttribute("value", "0");
        occidenticus.setAttribute("var", "autopilot.flight_time");
        Element orienticus = doc.createElement("go");
        orienticus.setAttribute("from", "HOME");
        orienticus.setAttribute("throttle", "1.0");
        orienticus.setAttribute("vmode", "throttle");
        orienticus.setAttribute("wp", "CLIMB");
        orienticus.setAttribute("pitch", "15");
        Element constantinus = doc.createElement("block");
        constantinus.setAttribute("key", "Ctrl+a");
        constantinus.setAttribute("name", "Standby");
        constantinus.setAttribute("strip_button", "Standby");
        constantinus.setAttribute("strip_icon", "mob.png");
        constantinus.setAttribute("group", "home");
        Element con = doc.createElement("circle");
        con.setAttribute("radius", "nav_radius");
        con.setAttribute("wp", "STDBY");
        Element belisarius = doc.createElement("block");
        belisarius.setAttribute("key", "F8");
        belisarius.setAttribute("name", "Making delivery");
        belisarius.setAttribute("strip_button", "Make delivery");
        belisarius.setAttribute("strip_icon", "eight.png");
        belisarius.setAttribute("group", "base_pattern");
        Element belis = doc.createElement("path");
        belis.setAttribute("wpts", "STDBY, DEL");
        belis.setAttribute("approaching_time", "0");
        Element germania = doc.createElement("set");
        germania.setAttribute("value", "1");
        germania.setAttribute("var", "drop_payload");
        Element heraclius = doc.createElement("block");
        heraclius.setAttribute("key", "F9");
        heraclius.setAttribute("name", "Returning to home");
        heraclius.setAttribute("strip_button", "Return home");
        heraclius.setAttribute("strip_icon", "home.png");
        heraclius.setAttribute("group", "home");
        Element heracli = doc.createElement("go");
        //heracli.setAttribute("throttle", "1.0");
        //heracli.setAttribute("vmode", "throttle");
        heracli.setAttribute("wp", "HOME");
        //heracli.setAttribute("pitch", "0");
        Element arshakuni = doc.createElement("deroute");
        arshakuni.setAttribute("block", "land");
        Element carolus = doc.createElement("block");
        carolus.setAttribute("name", "Land Right AF-TD");
        carolus.setAttribute("strip_button", "Land right (wp AF-TD)");
        carolus.setAttribute("strip_icon", "land-right.png");
        carolus.setAttribute("group", "land");
        Element carola = doc.createElement("set");
        carola.setAttribute("value", "DEFAULT_CIRCLE_RADIUS");
        carola.setAttribute("var", "nav_radius");
        Element karling = doc.createElement("deroute");
        karling.setAttribute("block", "land");
        Element otto = doc.createElement("block");
        otto.setAttribute("name", "Land Left AF-TD");
        otto.setAttribute("strip_button", "Land left (wp AF-TD)");
        otto.setAttribute("strip_icon", "land-left.png");
        otto.setAttribute("group", "land");
        Element ott = doc.createElement("set");
        ott.setAttribute("value", "DEFAULT_CIRCLE_RADIUS");
        ott.setAttribute("var", "nav_radius");
        Element liudolfing = doc.createElement("deroute");
        liudolfing.setAttribute("block", "land");
        Element rogerios = doc.createElement("block");
        rogerios.setAttribute("name", "land");
        Element roge = doc.createElement("call_once");
        roge.setAttribute("fun", "nav_compute_baseleg(WP_AF, WP_TD, WP__BASELEG, nav_radius)");
        //CDATASection rogeres = doc.createCDATASection("<circle radius=\"nav_radius\" until=\"NavCircleCount() > 0.5\" wp=\"_BASELEG\"/>");
        Element rogeres = doc.createElement("circle");
        rogeres.setAttribute("radius", "nav_radius");
        rogeres.setAttribute("until", "NavCircleCount() > 0.5");
        rogeres.setAttribute("wp", "_BASELEG");
        Element normanicus = doc.createElement("circle");
        normanicus.setAttribute("radius", "nav_radius");
        normanicus.setAttribute("until", "NavQdrCloseTo(DegOfRad(baseleg_out_qdr)-(nav_radius/fabs(nav_radius))*10) @AND (fabs(GetPosAlt() - WaypointAlt(WP__BASELEG)) @LT 10)");
        normanicus.setAttribute("wp", "_BASELEG");
        Element mehmed = doc.createElement("block");
        mehmed.setAttribute("name", "final");
        //CDATASection mech = doc.createCDATASection("<exception cond=\"GetAltRef() + 10 > GetPosAlt()\" deroute=\"flare\"/>");
        Element mech = doc.createElement("exception");
        mech.setAttribute("cond", "GetAltRef() + 10 > GetPosAlt()");
        mech.setAttribute("deroute", "flare");
        Element osman = doc.createElement("go");
        osman.setAttribute("from", "AF");
        osman.setAttribute("hmode", "route");
        osman.setAttribute("vmode", "glide");
        osman.setAttribute("wp", "TD");
        Element napoleon = doc.createElement("block");
        napoleon.setAttribute("name", "flare");
        Element napole = doc.createElement("go");
        napole.setAttribute("approaching_time", "0");
        napole.setAttribute("from", "AF");
        napole.setAttribute("hmode", "route");
        napole.setAttribute("throttle", "0.0");
        napole.setAttribute("vmode", "throttle");
        napole.setAttribute("wp", "TD");
        Element buonaparte = doc.createElement("attitude");
        buonaparte.setAttribute("roll", "0.0");
        buonaparte.setAttribute("throttle", "0.0");
        buonaparte.setAttribute("until", "FALSE");
        buonaparte.setAttribute("vmode", "throttle");
        julius.appendChild(july);
        julius.appendChild(julia);
        augustus.appendChild(august);
        augustus.appendChild(octavia);
        traianus.appendChild(traia);
        traianus.appendChild(ulpia);
        aurelianus.appendChild(aurel);
        aurelianus.appendChild(aurelia);
        aurelianus.appendChild(occidenticus);
        aurelianus.appendChild(orienticus);
        constantinus.appendChild(con);
        belisarius.appendChild(belis);
        belisarius.appendChild(germania);
        heraclius.appendChild(heracli);
        heraclius.appendChild(arshakuni);
        carolus.appendChild(carola);
        carolus.appendChild(karling);
        otto.appendChild(ott);
        otto.appendChild(liudolfing);
        rogerios.appendChild(roge);
        rogerios.appendChild(rogeres);
        rogerios.appendChild(normanicus);
        mehmed.appendChild(mech);
        mehmed.appendChild(osman);
        napoleon.appendChild(napole);
        napoleon.appendChild(buonaparte);
        five.appendChild(julius);
        five.appendChild(augustus);
        five.appendChild(traianus);
        five.appendChild(aurelianus);
        five.appendChild(constantinus);
        five.appendChild(belisarius);
        five.appendChild(heraclius);
        five.appendChild(carolus);
        five.appendChild(otto);
        five.appendChild(rogerios);
        five.appendChild(mehmed);
        five.appendChild(napoleon);
        one.appendChild(five);

        //doc.createElement("staff");
        //rootElement.appendChild(doc.createElement("test"));

        // write dom document to a file
        try (FileOutputStream output =
                     new FileOutputStream("flightplan.xml")) {
            writeXml(doc, output);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // write doc to output stream
    private static void writeXml(Document doc,
                                 OutputStream output)
            throws TransformerException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);

        transformer.transform(source, result);

    }


}
