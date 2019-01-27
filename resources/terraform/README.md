# Terraform for Agent Infrastructure

## Place variables/secrets in config/

Secrets are not commited to Github ;-)

Create `config/` dir with secrets/variables for terraform automation. 
Directory structure should in the end look like that:

```
terraform/
├── aws
│   ├── agent-network
│   │   ├── all.tf
│   │   ├── aws-agent-network.backend.config -> ../../config/aws-agent-network.backend.config                                            
│   │   ├── io.tf
│   │   └── terraform.tfvars -> ../../config/aws-agent-network.tfvars                                                                    
│   └── agent-vms
│       ├── all.tf
│       ├── aws-agent-vms.backend.config -> ../../config/aws-agent-vms.backend.config                                                    
│       ├── io.tf
│       └── terraform.tfvars -> ../../config/aws-agent-vms.tfvars
├── config
│   ├── aws-agent-network.backend.config
│   ├── aws-agent-network.tfvars
│   ├── aws-agent-vms.backend.config
│   └── aws-agent-vms.tfvars
├── Makefile
└── README.md
```

## Agent Network Infrastructure

To initialize the state:
```
cd aws/agent-network
terraform init -backend-config aws-agent-network.backend.config
```

To bootstrap the network:
```
cd aws/agent-network
terraform apply
```

## Agent VMs

Bootstrap agent-1:
```
cd aws/agent-vms
terraform workspace select 0
terraform apply
```

Bootstrap agent-2:
```
cd aws/agent-vms
terraform workspace select 1
terraform apply
```
