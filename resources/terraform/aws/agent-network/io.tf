variable "aws_access_key" {}
variable "aws_secret_key" {}
variable "aws_region" {}

variable "agents_vpc_cidr" {}
variable "agents_subnet_cidr" {}

variable "agent_access_public_key" {}

variable "agent_config_map" { type = "map" }
variable "num_agents" {}

### Output
output "agents_subnet_id" {
  value = "${aws_subnet.agents.id}"
}

output "agent_access_key_name" {
  value = "${aws_key_pair.agent-access.key_name}"
}

output "agent_public_ip_ids" {
  value = "${data.aws_eip.public-agent-ip.*.id}"
}

output "agent_config_map" {
  value = "${var.agent_config_map}"
}

output "num_agents" {
  value = "${var.num_agents}"
}

output "agent_security_group_id" {
  value = "${aws_security_group.agents.id}"
}
