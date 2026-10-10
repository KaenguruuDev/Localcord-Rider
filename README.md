# Localcord Rider

JetBrains Rider support for [Localcord](https://github.com/KaenguruuDev/Localcord)
scenario and fixture files.

The plugin adds file types, icons, language-server integration, native run
configurations, and run/debug actions for Localcord projects.

## Requirements

The Localcord CLI is installed separately from this plugin. The executable must
be available on the `PATH` inherited by Rider because the plugin starts both
`localcord run` and `localcord lsp` from the IDE.

You can verify the CLI from a terminal with:

```shell
localcord --version
```

Your project also needs a `localcord.runner.yaml`. By default, the plugin looks
for that file in the scenario's directory and then in each parent directory.
The first matching file is used. A run configuration can override this lookup
with an explicit runner configuration path.

If the runner configuration uses Docker Compose, Docker must also be available
to the Localcord CLI. The plugin does not install or manage the CLI, Docker, or
project dependencies.

## Features

* File association for Localcord files
* Custom file icons
* Separate file types for:

    * `.lc` — Localcord scenario files
    * `.lcf` — Localcord fixture files

* Localcord language-server support through `localcord lsp`
* Native Localcord run configurations with file choosers and runner options
* Run and debug gutter actions on the first `scenario:` line
* A `Run all Localcord scenarios` action for the complete runner plan

## Quick start

1. Install the plugin and the Localcord CLI.
2. Make sure Rider can find `localcord` on its `PATH`. If the CLI was installed
   after Rider started, restart Rider so it receives the updated environment.
3. Open a project containing a `.lc` file and a `localcord.runner.yaml`.
4. Open the `.lc` file and use the gutter action beside its first `scenario:`
   line.

The run action creates a temporary `Localcord` run configuration when no
matching configuration already exists. The debug action uses the same matching
configuration when one is available.

## Running scenarios

### Gutter actions

The first `scenario:` declaration in an `.lc` file has three actions:

* **Run Localcord scenario** runs the selected file through the native Localcord
  CLI runner.
* **Debug Localcord scenario** starts a Localcord DAP session through Rider's
  debugger integration.
* **Run all Localcord scenarios** runs the complete scenario list from the
  runner configuration instead of filtering to the current file.

Gutter-created configurations are temporary until you save them from Rider's
Run/Debug configuration UI. A reusable configuration is matched by its scenario
path and whether it represents one scenario or all scenarios.

### Reusable run configurations

For reusable settings, open `Run | Edit Configurations...`, add a `Localcord`
configuration, and save it. The configuration editor exposes the following
values:

| Setting | Purpose |
| --- | --- |
| Scenario | Required `.lc` file to run or debug. |
| Runner config | Optional `localcord.runner.yaml` override. If empty, ancestor lookup is used. |
| Environment file | Optional dotenv file passed to Localcord as `--env-file`. |
| Base URL | Optional absolute `http://` or `https://` URL. |
| Filter | Optional Localcord filter. If empty, a normal run targets the selected scenario. |
| Report format | Optional `console`, `junit`, or `both` report override. |
| JUnit output path | Optional path for JUnit XML output. |
| Artifacts path | Optional directory for failure artifact bundles. |
| Repeat | Optional number of times to repeat the plan. |
| Seed | Optional deterministic integer shuffle seed. |
| Shuffle scenarios | Randomizes scenario order when enabled. |
| Scenario timeout | Optional per-scenario timeout, such as `30s` or `2m`. |
| Parallelism | Optional maximum number of scenario workers. |
| Stop after the first failed scenario | Enables fail-fast behavior. |
| Disable Docker Compose orchestration | Passes `--no-compose`. |
| Reuse the Compose environment after the run | Passes `--reuse-environment`. |
| Enable verbose output | Passes `--verbose`. |
| Diagnostics level | Optional `off`, `failure`, `error`, `warning`, or `information` level. |

Empty optional fields are omitted from the native `localcord run` command.
Boolean options are passed only when enabled. The resulting command starts in
the directory containing the selected runner configuration.

### Run and debug settings

Native runs forward the complete set of options above to `localcord run`.
Debug sessions use Localcord's DAP integration. They share the selected
scenario, runner configuration, dotenv file, base URL, filter, Compose toggle,
and verbose setting; reporting, repeat, shuffle, timeout, parallelism,
fail-fast, environment-reuse, and diagnostics options are native-run options
and do not change the DAP launch request.

## File types

`.lc` files contain executable Localcord scenarios. The language server and
scenario gutter actions are associated with this file type.

`.lcf` files contain declarative Localcord fixture state. They are displayed as
Localcord fixture files and are also mapped to the Localcord language server.

## Configuration and path behavior

* Relative runner-config and dotenv paths are resolved relative to the selected
  scenario file's directory.
* With no explicit runner-config path, lookup starts at the scenario's
  directory and walks upward until it finds `localcord.runner.yaml`.
* A normal gutter run filters to the selected scenario unless a filter is
  supplied. **Run all Localcord scenarios** clears that implicit file filter.
* The plugin validates that the scenario, runner configuration, and selected
  dotenv file exist before starting a run. Base URLs must be absolute HTTP or
  HTTPS URLs.
* The plugin stores run-configuration values in Rider's normal run
  configuration state, so saved configurations can be shared with the project
  in the usual Rider way.

## Troubleshooting

### No gutter action appears

Confirm that the file is recognized as a Localcord `.lc` file and that it
contains a `scenario:` declaration. The actions are attached to the first
scenario declaration only.

### Rider cannot start `localcord`

Run `localcord --version` from a terminal, then check the environment available
to Rider. GUI-launched Rider instances may not receive shell profile changes;
restart Rider after changing `PATH` or configure the CLI installation so it is
visible to the IDE.

### The runner configuration is not found

Place `localcord.runner.yaml` in the scenario directory or one of its parents,
or select the file explicitly in the **Runner config** field. The file must be
named exactly `localcord.runner.yaml` when using automatic lookup.

### A dotenv file or scenario path cannot be found

Relative paths are based on the scenario file's directory. Check the path in
the run configuration and make sure the file exists before launching.

### Debugging does not start

Confirm that the Localcord CLI and its DAP support are installed, the scenario
and runner configuration pass validation, and the Localcord language server is
available. A native run is a useful first check before troubleshooting a DAP
session.

## Development

The project uses the Gradle wrapper. Run the test suite with:

```shell
./gradlew test
```

Run the full verification lifecycle with:

```shell
./gradlew check
```

To build an installable plugin distribution:

```shell
./gradlew buildPlugin
```

The current build targets JetBrains Rider `2026.2` and Java `25`; see
`build.gradle.kts` if you are setting up a development environment.

## License

See [LICENSE](LICENSE) for details.
