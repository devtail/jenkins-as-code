provider "aws" {
  access_key = "${var.aws_access_key}"
  secret_key = "${var.aws_secret_key}"
  region     = "${var.aws_region}"
}

terraform {
  backend "s3" {}
}

resource "aws_vpc" "agents" {
  cidr_block = "${var.agents_vpc_cidr}"
}

resource "aws_internet_gateway" "gw" {
  vpc_id = "${aws_vpc.agents.id}"
}

resource "aws_route_table" "agent-routing" {
  vpc_id = "${aws_vpc.agents.id}"
}

resource "aws_route" "internet" {
  route_table_id         = "${aws_route_table.agent-routing.id}"
  destination_cidr_block = "0.0.0.0/0"
  gateway_id             = "${aws_internet_gateway.gw.id}"
}

resource "aws_subnet" "agents" {
  vpc_id     = "${aws_vpc.agents.id}"
  cidr_block = "${var.agents_subnet_cidr}"
}

resource "aws_main_route_table_association" "agent-routing" {
  vpc_id         = "${aws_vpc.agents.id}"
  route_table_id = "${aws_route_table.agent-routing.id}"
}

resource "aws_security_group" "agents" {
  name        = "agent_rules"
  description = "Agent Rules"
  vpc_id      = "${aws_vpc.agents.id}"
}

resource "aws_security_group_rule" "allow-ssh" {
  type            = "ingress"
  from_port       = 22
  to_port         = 22
  protocol        = "tcp"
  cidr_blocks     = ["0.0.0.0/0"]

  security_group_id = "${aws_security_group.agents.id}"
}

resource "aws_security_group_rule" "allow_all" {
  type              = "egress"
  to_port           = 0
  protocol          = "-1"
  from_port         = 0
  cidr_blocks     = ["0.0.0.0/0"]

  security_group_id = "${aws_security_group.agents.id}"
}

resource "aws_key_pair" "agent-access" {
  key_name   = "devtail-jenkins-agent-access"
  public_key = "${var.agent_access_public_key}"
}

data "aws_eip" "public-agent-ip" {
  count = "${length(keys(var.agent_config_map)) / var.num_agents}"

  filter {
    name   = "tag:Name"
    values = ["${var.agent_config_map["${count.index}_name"]}"]
  }
}
