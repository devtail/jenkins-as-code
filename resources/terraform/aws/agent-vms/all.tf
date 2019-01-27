provider "aws" {
  access_key = "${var.aws_access_key}"
  secret_key = "${var.aws_secret_key}"
  region     = "${var.aws_region}"
}

terraform {
  backend "s3" {}
}

data "terraform_remote_state" "agent-network" {
  backend = "s3"
  config {
    bucket     = "${var.remote_state_agent_network_s3_bucket}"
    key        = "${var.remote_state_agent_network_s3_key}"
    region     = "${var.remote_state_agent_network_region}"
    access_key = "${var.remote_state_agent_network_access_key}"
    secret_key = "${var.remote_state_agent_network_secret_key}"
  }
}

locals {
  agent_config_map = "${data.terraform_remote_state.agent-network.agent_config_map}"
  num_agents = "${data.terraform_remote_state.agent-network.num_agents}"
}

data "aws_ami" "ubuntu18-04" {
  most_recent = true

  filter {
    name   = "name"
    values = ["ubuntu/images/hvm-ssd/ubuntu-bionic-18.04-amd64-server-*"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }

  owners = ["099720109477"] # Canonical
}

resource "aws_instance" "agent" {
  ami                         = "${data.aws_ami.ubuntu18-04.id}"
  instance_type               = "${local.agent_config_map["${terraform.workspace}_size"]}"
  subnet_id                   = "${data.terraform_remote_state.agent-network.agents_subnet_id}"
  key_name                    = "${data.terraform_remote_state.agent-network.agent_access_key_name}"
  user_data                   = "${file("user_data/docker.sh")}"
  vpc_security_group_ids      = [
    "${data.terraform_remote_state.agent-network.agent_security_group_id}"
  ]

  tags = {
    Name = "${local.agent_config_map["${terraform.workspace}_name"]}"
  }
}

resource "aws_eip_association" "agent" {
  instance_id   = "${aws_instance.agent.id}"
  allocation_id = "${data.terraform_remote_state.agent-network.agent_public_ip_ids[terraform.workspace]}"
}
