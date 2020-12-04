# What is gP2S #
gP2S (for [Genentech’s](http://www.gene.com) Protein to Structure) is a user-friendly web-based laboratory information management system
([LIMS](https://en.wikipedia.org/wiki/Laboratory_information_management_system))
that facilitates accurate record-keeping for cryo-EM labs and multi-user, multi-project facilities. 

The following entities, their relationships and associated metadata are tracked: projects, equipment, consumables,
protocols, samples, grids, microscopy sessions, image processing sessions, maps, and atomic models. Users can also
add free-text comments, optionally including file attachments, allowing for rich annotation of any entity registered
in gP2S.

The front-end has been designed to facilitate use with touchscreen devices and tested extensively on 12.9” iPad Pros,
making it possible to use gP2S at the lab bench while preparing samples and grids, as well as at the computer when
operating the microscope, processing images or depositing models.

The back-end features a number of REST API endpoints, making it possible to integrate gP2S into existing workflows
and scripts.

In the initial release of gP2S, users enter data manually but with as many parameters as possible preset to sensible
default values. We hope that future versions will minimize manual input by interacting directly with hardware
(e.g. microscopes), software (e.g. SerialEM, EPU, Latitude), and files (e.g. star files). We are also planning to
support semi-automated (XML-based) structure deposition to EMDB. 

gP2S is still under development at Genentech. We welcome feedback, bug reports and feature requests.

# gP2S  application as docker services #
The easiest way to start working in gP2S web application is to run it as docker services. We provide
comprehensive `README-DOCKER.MD` file that will guide you through the startup process, so that you will need just a few commands
to start the application with all the dependencies and with a minimum footprint on your host.

# Building gP2S from sources #
In order to build the gP2S application on your own, check our `README-BUILD.MD`

# I started the application, what's next? #
Once you have gP2S up and running, it is like a blank slate: it doesn’t know anything about your projects, what microscopes and other equipment your facility has, etc. Before you can start using it to track actual experiments, you will need to spend a few minutes telling gP2S about all these things. So the first thing you should do is navigate to the “Settings” section and work through all the sections to enter information about your lab’s setup. 

At a minimum, you will need to define a project. This will enable you to create proteins, ligands and samples in gP2S.

To enable the creation of grids, you will also need to tell gP2S about Protocols (surface treatment, negative stain, vitrification), Equipment (glow-discharge or plasma cleaners, called “surface treatment machines”, vitrification machines, cryo storage devices), Consumables (grid types, blotting paper). 

For each type of entity (e.g. “Vitrification Machine”, “Grid Type”), navigate to the relevant page by using the left-hand-side menu, click the “Create New” button in the top left of the page, and fill in the form that appears. Fields with red asterisks are required; others are optional. Then click the “Save” button at the top of the page when you are done.

To enable microscopy sessions, you will need to tell gP2S about your microscope(s), which in turn entails creating electron detector(s) and sample holder(s). When creating the microscope object, you have the opportunity to enter the list of magnifications for this microscope. You only need to enter those magnifications you want users to be able to use during data collection. 

# Issues #
Any issues can be reported using the GitHub standard issue management mechanism.
  