package com.acfapi.entity;

public class QuotaDefinition {
	private String name;
	private boolean non_basic_services_allowed;
	private int total_services;
	private int total_service_keys;
	private int total_routes;
	private int total_reserved_route_ports;
	private int total_private_domains;
	private int memory_limit;
	private int instance_memory_limit;
	private int app_instance_limit;
	private int app_task_limit;
	
	/*public QuotaDefinition(QuotaDefinition quota) {
		super();
		this.name =quota.name;
		this.non_basic_services_allowed = quota.non_basic_services_allowed;
		this.total_services = quota.total_services;
		this.total_service_keys = quota.total_service_keys;
		this.total_routes = quota.total_routes;
		this.total_reserved_route_ports = quota.total_reserved_route_ports;
		this.total_private_domains = quota.total_private_domains;
		this.memory_limit = quota.memory_limit;
		this.instance_memory_limit = quota.instance_memory_limit;
		this.app_instance_limit = quota.app_instance_limit;
		this.app_task_limit = quota.app_task_limit;
	}*/
	
	
  
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isNon_basic_services_allowed() {
		return non_basic_services_allowed;
	}
	public void setNon_basic_services_allowed(boolean non_basic_services_allowed) {
		this.non_basic_services_allowed = non_basic_services_allowed;
	}
	public int getTotal_services() {
		return total_services;
	}
	public void setTotal_services(int total_services) {
		this.total_services = total_services;
	}
	public int getTotal_service_keys() {
		return total_service_keys;
	}
	public void setTotal_service_keys(int total_service_keys) {
		this.total_service_keys = total_service_keys;
	}
	public int getTotal_routes() {
		return total_routes;
	}
	public void setTotal_routes(int total_routes) {
		this.total_routes = total_routes;
	}
	public int getTotal_reserved_route_ports() {
		return total_reserved_route_ports;
	}
	public void setTotal_reserved_route_ports(int total_reserved_route_ports) {
		this.total_reserved_route_ports = total_reserved_route_ports;
	}
	public int getTotal_private_domains() {
		return total_private_domains;
	}
	public void setTotal_private_domains(int total_private_domains) {
		this.total_private_domains = total_private_domains;
	}
	public int getMemory_limit() {
		return memory_limit;
	}
	public void setMemory_limit(int memory_limit) {
		this.memory_limit = memory_limit;
	}
	public int getInstance_memory_limit() {
		return instance_memory_limit;
	}
	public void setInstance_memory_limit(int instance_memory_limit) {
		this.instance_memory_limit = instance_memory_limit;
	}
	public int getApp_instance_limit() {
		return app_instance_limit;
	}
	public void setApp_instance_limit(int app_instance_limit) {
		this.app_instance_limit = app_instance_limit;
	}
	public int getApp_task_limit() {
		return app_task_limit;
	}
	public void setApp_task_limit(int app_task_limit) {
		this.app_task_limit = app_task_limit;
	}

	
	
	
	
	
	
}
