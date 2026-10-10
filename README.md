# Localcord Rider

JetBrains Rider support for [Localcord](https://github.com/KaenguruuDev/Localcord) files.

This plugin provides basic IDE integration for Localcord's custom DSL and fixture formats.

## Features

* File association for Localcord files
* Custom file icons
* Separate file types for:

    * `.lc` — Localcord scenario files
    * `.lcf` — Localcord fixture files
* Native Localcord run configurations with file chooser and runner options
* Run and debug gutter actions on the first `scenario:` line

Language functionality is provided by `localcord lsp`. Running scenarios requires
the Localcord CLI to be installed and available on the IDE's `PATH`.

## Running scenarios

Open a `.lc` file and use the gutter action beside its first `scenario:` line.
The run action creates a temporary Localcord run configuration automatically.
The debug action uses the same Localcord configuration as a normal run; Rider's
selected executor chooses between the native runner and the LSP4IJ debugger.
The `Run all Localcord scenarios` action uses the runner configuration's complete
scenario list instead of filtering to the current file.

For reusable settings, create a `Localcord` run configuration from the Run/Debug
configuration dialog. It supports:

* Scenario file
* Optional `localcord.runner.yaml` override
* Optional dotenv environment file
* Optional base URL
* Optional scenario filter
* Optional report format, JUnit output path, and failure artifacts path
* Optional repeat, seed, shuffle, scenario timeout, and parallelism overrides
* Optional fail-fast, environment reuse, and diagnostics level switches
* Disabling Compose orchestration
* Verbose output

In Rider, open `Run | Edit Configurations...` and select or add a `Localcord`
configuration. The gutter actions use this same configuration for both run and
debug.

If no runner configuration override is supplied, the plugin searches the
scenario's directory and its ancestors for `localcord.runner.yaml`. The settings
from a matching Localcord run configuration are also applied when starting a
debug session from the gutter.

## File Types

`.lc`: Localcord scenario files describe executable test scenarios.

`.lcf`: Localcord fixture files define declarative test state.

## License

See the repository's license file for details.
