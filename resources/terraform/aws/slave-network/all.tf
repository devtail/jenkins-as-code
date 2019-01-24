provider "aws" {
  access_key = "${var.aws_access_key}"
  secret_key = "${var.aws_secret_key}"
  region     = "${var.aws_region}"
}

terraform {
  backend "s3" {}
}

resource "aws_vpc" "slaves" {
  cidr_block = "${var.slaves_vpc_cidr}"
}

resource "aws_subnet" "slaves" {
  vpc_id     = "${aws_vpc.slaves.id}"
  cidr_block = "${var.slaves_subnet_cidr}"
}
