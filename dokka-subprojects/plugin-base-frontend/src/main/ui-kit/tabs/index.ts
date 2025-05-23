/*
 * Copyright 2014-2024 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */
import './styles.scss';
import { debounce } from '../utils';

export function initTabs() {
  // we could have only a single type of data - classlike or package
  const mainContent = document.querySelector('.main-content');
  const type = mainContent ? mainContent.getAttribute('data-page-type') : null;
  const localStorageKey = 'active-tab-' + type;
  document.querySelectorAll('div[tabs-section]').forEach((element) => {
    showCorrespondingTabBody(element);
    element.addEventListener('click', ({ target }) => {
      const togglable = target ? (target as Element).getAttribute('data-togglable') : null;
      if (!togglable) {
        return;
      }

      safeLocalStorage.setItem(localStorageKey, JSON.stringify(togglable));
      toggleSections(target as Element);
    });
  });

  const cached = safeLocalStorage.getItem(localStorageKey);
  if (!cached) {
    return;
  }

  const tab = document.querySelector('div[tabs-section] > button[data-togglable="' + JSON.parse(cached) + '"]');
  if (!tab) {
    return;
  }

  toggleSections(tab);
}

function showCorrespondingTabBody(element: Element) {
  const buttonWithKey = element.querySelector('button[data-active]');
  if (buttonWithKey) {
    toggleSections(buttonWithKey);
  }
}

export function toggleSections(target: Element) {
  const activateTabs = (containerClass: string) => {
    for (const element of document.getElementsByClassName(containerClass)) {
      for (const child of element.children) {
        if (child.getAttribute('data-togglable') === target.getAttribute('data-togglable')) {
          child.setAttribute('data-active', '');
        } else {
          child.removeAttribute('data-active');
        }
      }
    }
  };
  const toggleTargets = target.getAttribute('data-togglable')?.split(',');
  const activateTabsBody = (containerClass: string) => {
    document.querySelectorAll('.' + containerClass + ' *[data-togglable]').forEach((child) => {
      const childTarget = child.getAttribute('data-togglable');
      if (toggleTargets && childTarget && toggleTargets.includes(childTarget)) {
        child.setAttribute('data-active', '');
      } else if (!child.classList.contains('sourceset-dependent-content')) {
        // data-togglable is used to switch sourceset as well, ignore it
        child.removeAttribute('data-active');
      }
    });
  };
  activateTabs('tabs-section');
  activateTabsBody('tabs-section-body');

  const TABS_DEFAULT_HEIGHT = 41;
  const TABS_WRAPPING_CLASS = 'tabs_wrapping';
  const sectionTabsRows = document.querySelectorAll('div.tabs-section');
  const platformTabsRows = document.querySelectorAll('div.platform-bookmarks-row');
  const tabsRows = [...sectionTabsRows, ...platformTabsRows];

  function processTabsWrapping(tabsRow: Element): void {
    const tabsRowHeight = tabsRow.getBoundingClientRect().height;
    if (tabsRowHeight > TABS_DEFAULT_HEIGHT) {
      tabsRow.classList.add(TABS_WRAPPING_CLASS);
    } else {
      tabsRow.classList.remove(TABS_WRAPPING_CLASS);
    }
  }

  const resizeObservers: ResizeObserver[] = [];
  for (let i = 0; i < tabsRows.length; i++) {
    const debouncedProcessTabsWrapping = debounce(processTabsWrapping, 100);
    const resizeObserver = new ResizeObserver(() => {
      debouncedProcessTabsWrapping(tabsRows[i]);
    });
    resizeObservers.push(resizeObserver);
  }

  const initResizeObservers = (): void => {
    for (let i = 0; i < resizeObservers.length; i++) {
      resizeObservers[i].observe(tabsRows[i]);
    }
  };

  initResizeObservers();
}

// eslint-disable-next-line @typescript-eslint/no-explicit-any
(window as any).initTabs = initTabs;
// eslint-disable-next-line @typescript-eslint/no-explicit-any
(window as any).toggleSections = toggleSections;
