variable "aws_access_key" {}
variable "aws_secret_key" {}
variable "aws_region" {}

variable "slaves_vpc_cidr" {}
variable "slaves_subnet_cidr" {}

output "slaves_subnet_id" {
  value = "${aws_subnet.slaves.id}"
}
