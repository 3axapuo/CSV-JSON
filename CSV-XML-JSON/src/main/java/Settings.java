import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Settings {
    protected final String[] load; // массив настроек загрузки
    protected final String[] save; // массив настроек сохранения
    protected String[] log; // массив настроек журнала

    Settings(int load, int save, int log) {
        this.load = new String[load];
        this.save = new String[save];
        this.log = new String[log];
    }

    public void read(Node node) {
        NodeList nodeList = node.getChildNodes(); //
        for (int i = 0; i < nodeList.getLength(); i++) { // Просматриваем все подэлементы корневого узла
            Node currentNode = nodeList.item(i);
            if (Node.ELEMENT_NODE == currentNode.getNodeType()) {
                if (currentNode.getNodeType() != Node.TEXT_NODE) {
                    NodeList bookProps = currentNode.getChildNodes();
                    for (int j = 0; j < bookProps.getLength(); j++) {
                        Node bookProp = bookProps.item(j);
                        if (bookProp.getNodeType() != Node.TEXT_NODE) {
                            String settings = currentNode.getNodeName();
                            String tag = bookProp.getNodeName();
                            String value = bookProp.getChildNodes().item(0).getTextContent();
                            getValue(settings, tag, value); // вызываем метод для извлечения значений
                        }
                    }
                }
                Element element = (Element) currentNode;
                NamedNodeMap map = element.getAttributes();
                for (int a = 0; a < map.getLength(); a++) {
                    String attrName = map.item(a).getNodeName();
                    String attrValue = map.item(a).getNodeValue();
                    System.out.println("Атрибут: " + attrName + "; значение: " + attrValue);
                }
                read(currentNode);
            }
        }
    }

    public void getValue(String settings, String tag, String value) {
        // записываем в массив настройки загрузки
        if (settings.equals("load") && tag.equals("enabled"))
            this.load[0] = value; // загрузка сохраненной корзины при запуске, boolean значение
        if (settings.equals("load") && tag.equals("fileName"))
            this.load[1] = value;// имя файла сохраненной корзины, basket.json ИЛИ basket.txt
        if (settings.equals("load") && tag.equals("format"))
            this.load[2] = value; // наименование формата, JSON или TEXT
        // записываем в массив настройки сохранения
        if (settings.equals("save") && tag.equals("enabled"))
            this.save[0] = value; // сохранение корзины при выходе, boolean значение
        if (settings.equals("save") && tag.equals("fileName"))
            this.save[1] = value; // имя файла сохраненной корзины, basket.json ИЛИ basket.txt
        if (settings.equals("save") && tag.equals("format"))
            this.save[2] = value; // наименование формата сохранения корзины, JSON или TEXT
        // записываем в массив настройки журнала
        if (settings.equals("log") && tag.equals("enabled"))
            this.log[0] = value; // сохранение журнала при выходе, boolean значение
        if (settings.equals("log") && tag.equals("fileName"))
            this.log[1] = value; // имя файла формата *.csv, client.csv"
    }

}
