package com.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.bean.SystemInfo;
import com.dao.impl.BaseDaoImpl;

public class SystemInitListener implements ServletContextListener {

    public SystemInitListener() {
    	
    }

    public void contextInitialized(ServletContextEvent sce)  { 
    	ServletContext application = sce.getServletContext();

    	SystemInfo sys = (SystemInfo) new BaseDaoImpl().getObject(SystemInfo.class, "SELECT * FROM system", null);

    	application.setAttribute("systemInfo", sys);
    }

    public void contextDestroyed(ServletContextEvent sce)  { 
         
    }
	
}
