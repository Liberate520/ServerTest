package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
		// порт, который будет прослушивать наш сервер
    static final int PORT = 8081;

    public Server() {
				// серверный сокет
        ServerSocket serverSocket = null;
        Socket clientSocket1 = null;
        Socket clientSocket2 = null;
        try {
                // создаём серверный сокет на определенном порту
            serverSocket = new ServerSocket(PORT);
            System.out.println("Сервер запущен!");

//            Socket clientSocket = serverSocket.accept();
//            System.out.println("Кто то подключился");
						// запускаем бесконечный цикл
            while (true) {

                boolean gamePlayerNotComplete = true;
                clientSocket1 = null;
                clientSocket2 = null;
                while (gamePlayerNotComplete) {

                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Кто-то подключился");
                    if (clientSocket1 == null || !clientSocket1.isConnected()) {
                        clientSocket1 = clientSocket;
                        System.out.println("Подключился первый");
                    } else {
                        clientSocket2 = clientSocket;
                        gamePlayerNotComplete = false;
                        new Thread(new GameHandler(clientSocket1, clientSocket2)).start();
                        System.out.println("Подключился второй");
                        System.out.println("Игра запущена");
                    }
                }
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        finally {
            try {
								// закрываем подключение
                System.out.println("Сервер остановлен");
                serverSocket.close();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
