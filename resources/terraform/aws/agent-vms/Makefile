get-workspace:
	terraform init -backend-config aws-agent-vms.backend.config
	terraform workspace new $(AGENT_ID) || true
	terraform workspace select $(AGENT_ID)

bootstrap-agent-vm: get-workspace
	terraform apply -auto-approve

destroy-agent-vm: get-workspace
	terraform destroy -auto-approve

