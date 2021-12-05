package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class GameHandler implements Runnable {

    // исходящее сообщение
    private PrintWriter outMessage1, outMessage2;
    // входящее собщение
    private Scanner inMessage1, inMessage2;
    // клиентские сокеты
    private Socket clientSocket1 = null;
    private Socket clientSocket2 = null;
    // продолжается игра
    private boolean gameContinue = true;

    // конструктор, который принимает клиентский сокет и сервер
    public GameHandler(Socket clientSocket1, Socket clientSocket2) {
        try {
            this.clientSocket1 = clientSocket1;
            this.clientSocket2 = clientSocket2;
            this.outMessage1 = new PrintWriter(clientSocket1.getOutputStream());
            this.outMessage2 = new PrintWriter(clientSocket2.getOutputStream());
            this.inMessage1 = new Scanner(clientSocket1.getInputStream());
            this.inMessage2 = new Scanner(clientSocket2.getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    // Переопределяем метод run(), который вызывается когда
    // мы вызываем new Thread(client).start();
    @Override
    public void run() {
        try {
                // сервер отправляет сообщение
                sendMsg(1, "Первый игрок");
                sendMsg(2, "Второй игрок");


            while (gameContinue) {
                // Если от клиента пришло сообщение
                if (inMessage1.hasNext()) {
                    String clientMessage = inMessage1.nextLine();
                    // если клиент отправляет данное сообщение, то цикл прерывается
                    if (clientMessage.equalsIgnoreCase("##session##end##")) {
                        sendMsg(2, "playerLeave");
                        break;
                    }
                    // выводим в консоль сообщение (для теста)
                    System.out.println(clientMessage);
                    // отправляем данное сообщение всем клиентам
                    sendMsg(2, clientMessage);
                }
                if (inMessage2.hasNext()) {
                    String clientMessage = inMessage2.nextLine();
                    // если клиент отправляет данное сообщение, то цикл прерывается
                    if (clientMessage.equalsIgnoreCase("##session##end##")) {
                        sendMsg(1, "playerLeave");
                        break;
                    }
                    // выводим в консоль сообщение (для теста)
                    System.out.println(clientMessage);
                    // отправляем данное сообщение всем клиентам
                    sendMsg(1, clientMessage);
                }
                // останавливаем выполнение потока на 10 мс
                Thread.sleep(10);
            }
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        finally {
            try {
                clientSocket1.close();
                clientSocket2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.close();
        }
    }
    // отправляем сообщение
    public void sendMsg(int player, String msg) {
        try {
            if (player == 1){
                outMessage1.println(msg);
                outMessage1.flush();
            } else {
                outMessage2.println(msg);
                outMessage2.flush();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    // клиент выходит
    public void close() {

    }
}
