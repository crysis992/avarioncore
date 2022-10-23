/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.internal.network.HttpServer can not be copied and/or distributed without the express
 *  permission of crysis992
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of AvarionCraft.de and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to AvarionCraft.de
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from AvarionCraft.de.
 *
 */

package net.crytec.internal.network;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;

public class HttpServer extends Thread {

  private volatile boolean running = true;

  protected final int port;
  protected final ServerSocket socket;

  public HttpServer(int port) throws IOException {
    this.port = port;
    socket = new ServerSocket(port);
    socket.setReuseAddress(true);
  }

  @Override
  public void run() {
    while (running) {
      try {
        new Thread(new PhoenixConnection(this, socket.accept())).start();
      } catch (IOException e) {
        Bukkit.getLogger().warning("A thread was interrupted in the http daemon!");
      }
    }
    if (!socket.isClosed()) {
      try {
        socket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void terminate() {
    running = false;
    if (!socket.isClosed()) {
      try {
        socket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public File requestFileCallback(PhoenixConnection connection, String request) {
    return null;
  }

  public void onSuccessfulRequest(PhoenixConnection connection, String request) {
  }

  public void onClientRequest(PhoenixConnection connection, String request) {
  }

  public void onRequestError(PhoenixConnection connection, int code) {
  }

  public class PhoenixConnection implements Runnable {

    protected final HttpServer server;
    protected final Socket client;

    public PhoenixConnection(HttpServer server, Socket client) {
      this.server = server;
      this.client = client;
    }

    public Socket getClient() {
      return client;
    }

    @Override
    public void run() {
      try {
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream(), "8859_1"));
        OutputStream out = client.getOutputStream();
        PrintWriter pout = new PrintWriter(new OutputStreamWriter(out, "8859_1"), true);
        String request = in.readLine();
        onClientRequest(this, request);

        Matcher get = Pattern.compile("GET /?(\\S*).*").matcher(request);
        if (get.matches()) {
          request = get.group(1);
          File result = requestFileCallback(this, request);
          if (result == null) {
            pout.println("HTTP/1.0 400 Bad Request");
            onRequestError(this, 400);
          } else {
            try (FileInputStream fis = new FileInputStream(result)) {
              // Writes zip files specifically;
              out.write("HTTP/1.0 200 OK\r\n".getBytes());
              out.write("Content-Type: application/zip\r\n".getBytes());
              out.write(("Content-Length: " + result.length() + "\r\n").getBytes());
              out.write(("Date: " + new Date().toInstant() + "\r\n").getBytes());
              out.write("Server: PhoenixHttpd\r\n\r\n".getBytes());
              byte[] data = new byte[64 * 1024];
              for (int read; (read = fis.read(data)) > -1; ) {
                out.write(data, 0, read);
              }
              out.flush();
              onSuccessfulRequest(this, request);
            } catch (FileNotFoundException e) {
              pout.println("HTTP/1.0 404 Object Not Found");
              onRequestError(this, 404);
            }
          }
        } else {
          pout.println("HTTP/1.0 400 Bad Request");
          onRequestError(this, 400);
        }
        client.close();
      } catch (IOException e) {
        System.out.println("Oh no, it's broken D: " + e);
      }
    }
  }
}