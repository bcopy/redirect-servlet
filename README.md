# redirect-servlet.war

Use this when you depressingly find out that you can't use an HTTP server for
your redirects and instead are limited to your Java EE application server.

When deployed, it will redirect any path inside the application's context root
to a configurable new URL, including parameters.

Example:

| WAR context root  | ```new.base.url```          | User enters                                               | Redirected to                                            |
| ----------------- | --------------------------- | --------------------------------------------------------- | -------------------------------------------------------- |
| /redirect-servlet | http://newsite.com          | http://oldsite.com/redirect-servlet/index.html?page=about | http://newsite.com/index.html?page=about                 |
| /redirect-servlet | http://newsite.com          | http://oldsite.com/redirect-servlet/page/about/me         | http://newsite.com/page/about/me                         |
| /redirect-servlet | / _(or blank)_              | http://oldsite.com/redirect-servlet/mypage.html           | http://oldsite.com/mypage.html                           |
| /redirect-servlet | /some-servlet               | http://oldsite.com/redirect-servlet/mypage.html           | http://oldsite.com/some-servlet/mypage.html              |
| /redirect-servlet | new-path                    | http://oldsite.com/redirect-servlet/mypage.html           | http://oldsite.com/redirect-servlet/new-path/mypage.html |
| /                 | http://newsite.com/wiki     | http://oldsite.com/                                       | http://newsite.com/wiki/                                 |
| /example          | http://newsite.com/personal | http://oldsite.com/example/mypage.html                    | http://newsite.com/personal/mypage.html                  |

## Compatibility

Java EE 5 / Servlet 2.5 and higher (tested on WebSphere Application Server 7)

Lots of quirks on J2EE 1.4 / Servlet 2.4 (modification required for WebSphere
Application Server 6.1)

## Usage

Update ```src/main/webapp/WEB-INF/application.properties``` with your desired
```new.base.url```.  This is formatted at runtime to remove a trailing ```/```
if it is present.

Run ```mvn clean package``` to build the WAR.

Deploy to your application server.  You may have to rename the WAR depending
on how your container determines the context root.

