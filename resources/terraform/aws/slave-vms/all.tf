provider "aws" {
  access_key = "${var.aws_access_key}"
  secret_key = "${var.aws_secret_key}"
  region     = "${var.aws_region}"
}

terraform {
  backend "s3" {}
}

data "terraform_remote_state" "slave-network" {
  backend = "s3"
  config {
    bucket     = "${var.remote_state_slave_network_s3_bucket}"
    key        = "${var.remote_state_slave_network_s3_key}"
    region     = "${var.remote_state_slave_network_region}"
    access_key = "${var.remote_state_slave_network_access_key}"
    secret_key = "${var.remote_state_slave_network_secret_key}"
  }
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

resource "aws_instance" "slave" {
  ami           = "${data.aws_ami.ubuntu18-04.id}"
  instance_type = "t2.micro"
  subnet_id     = "${data.terraform_remote_state.slave-network.slaves_subnet_id}"

  tags = {
    Name = "Jenkins Slave VM"
  }
}
