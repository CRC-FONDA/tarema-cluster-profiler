resource "google_compute_instance" "vm_instance" {
  name         = "terraform-instance-1"
  machine_type = "e2-highcpu-2"

  boot_disk {
    initialize_params {
      image = "debian-cloud/debian-10"
      size = 15
    }
  }

  network_interface {
    # A default network is created for all GCP projects
    network = "default"
    access_config {
    }
  }

  metadata = {
    ssh-keys = "${var.gce_ssh_user}:${file(var.gce_ssh_pub_key_file)}"
  }

}

resource "google_compute_instance" "vm_instance1" {
  name         = "terraform-instance-2"
  machine_type = "e2-highmem-2"

  boot_disk {
    initialize_params {
      image = "debian-cloud/debian-10"
      size = 15
    }
  }

  network_interface {
    # A default network is created for all GCP projects
    network = "default"
    access_config {
    }
  }

  metadata = {
    ssh-keys = "${var.gce_ssh_user}:${file(var.gce_ssh_pub_key_file)}"
  }

}

resource "google_compute_instance" "vm_instance2" {
  name         = "terraform-instance-3"
  machine_type = "e2-standard-2"

  boot_disk {
    initialize_params {
      image = "debian-cloud/debian-10"
      size = 15
    }
  }

  network_interface {
    # A default network is created for all GCP projects
    network = "default"
    access_config {
    }
  }

  metadata = {
    ssh-keys = "${var.gce_ssh_user}:${file(var.gce_ssh_pub_key_file)}"
  }

}

resource "google_compute_instance" "vm_instance3" {
  name         = "terraform-instance-4"
  machine_type = "e2-medium"
  

  boot_disk {
    initialize_params {
      image = "debian-cloud/debian-10"
      size = 15
    }
  }

  network_interface {
    # A default network is created for all GCP projects
    network = "default"
    access_config {
    }
  }

  metadata = {
    ssh-keys = "${var.gce_ssh_user}:${file(var.gce_ssh_pub_key_file)}"
  }

}

resource "google_compute_instance" "vm_instance4" {
  name         = "terraform-instance-5"
  machine_type = "e2-standard-4"
  zone         = "europe-west1-b"

  boot_disk {
    initialize_params {
      image = "debian-cloud/debian-10"
      size = 15
    }
  }

  network_interface {
    # A default network is created for all GCP projects
    network = "default"
    access_config {
    }
  }

  metadata = {
    ssh-keys = "${var.gce_ssh_user}:${file(var.gce_ssh_pub_key_file)}"
  }

}

resource "google_compute_instance" "vm_instance5" {
  name         = "terraform-instance-6"
  machine_type = "e2-micro"
  zone         = "europe-west1-b"

  boot_disk {
    initialize_params {
      image = "debian-cloud/debian-10"
      size = 15
    }
  }

  network_interface {
    # A default network is created for all GCP projects
    network = "default"
    access_config {
    }
  }

  metadata = {
    ssh-keys = "${var.gce_ssh_user}:${file(var.gce_ssh_pub_key_file)}"
  }

}

resource "google_compute_instance" "vm_instance6" {
  name         = "terraform-instance-7"
  machine_type = "e2-micro"
  zone         = "europe-west1-b"

  boot_disk {
    initialize_params {
      image = "debian-cloud/debian-10"
      size = 15
    }
  }

  network_interface {
    # A default network is created for all GCP projects
    network = "default"
    access_config {
    }
  }

  metadata = {
    ssh-keys = "${var.gce_ssh_user}:${file(var.gce_ssh_pub_key_file)}"
  }

}

resource "google_compute_instance" "vm_instance7" {
  name         = "terraform-instance-8"
  machine_type = "e2-micro"
  zone         = "europe-west1-b"

  boot_disk {
    initialize_params {
      image = "debian-cloud/debian-10"
      size = 15
    }
  }

  network_interface {
    # A default network is created for all GCP projects
    network = "default"
    access_config {
    }
  }

  metadata = {
    ssh-keys = "${var.gce_ssh_user}:${file(var.gce_ssh_pub_key_file)}"
  }

}


